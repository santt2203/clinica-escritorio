# Clínica — escritorio, SOAP y web

Proyecto Maven para Java 21 que conserva la aplicación de escritorio de la primera parte y agrega:

- un servicio SOAP Jakarta XML Web Services desplegable en Tomcat 10.1;
- un cliente web Servlet 6/JSP que consume exclusivamente ese servicio;
- los seis casos opcionales: búsqueda, detalle, ordenamiento sin recarga, seguidas sin recarga, solicitud/historial y diseño adaptable a teléfonos.

## Arquitectura

```text
clinica-core       dominio, persistencia, datatypes y aplicación Swing
      ↑
clinica-servicio   fachada SOAP, contrato y autorización por token

WSDL generado ──wsimport──> clinica-web (Servlets/JSP, sin acceso a la base)
```

El dominio existe una sola vez, bajo `clinica-core/src/main/java`. El servicio reutiliza ese módulo. El módulo web no depende de `clinica-core`, Hibernate ni PostgreSQL.

Las clases de `cliente.soap` se generan dentro de `clinica-web/target/generated-sources/wsimport` en cada compilación y no se versionan.

## Requisitos

- JDK 21
- Maven 3.9 o superior
- Docker Desktop con Docker Compose

Los contenedores usan PostgreSQL 16 y Tomcat 10.1 con Java 21. Los puertos publicados son:

| Componente | Dirección |
|---|---|
| Base PostgreSQL | `localhost:5433` |
| WSDL SOAP | `http://localhost:8081/clinica-servicio/soap/ClinicaService?wsdl` |
| Aplicación web | `http://localhost:8080/clinica-web/` |

## Orden de arranque

Desde la raíz del repositorio:

1. Levantar primero la base de datos.

   ```bash
   docker compose up -d db
   ```

2. Compilar todo. `wsgen` crea el WSDL del servicio y después `wsimport` regenera el cliente Java; el servicio no necesita estar levantado para compilar.

   ```bash
   mvn clean package
   ```

3. Levantar el servidor que publica SOAP.

   ```bash
   docker compose up -d servicio
   ```

   Antes de seguir, abrir el WSDL o esperar hasta que responda:
   `http://localhost:8081/clinica-servicio/soap/ClinicaService?wsdl`.

4. Por último levantar el cliente web.

   ```bash
   docker compose up -d web
   ```

5. Abrir `http://localhost:8080/clinica-web/`.

Para detener todo:

```bash
docker compose down
```

Para eliminar además todos los datos locales de PostgreSQL:

```bash
docker compose down -v
```

Ese último comando es destructivo y no se debe usar si hay datos que conservar.

## Si el servicio SOAP no está disponible

El sitio puede desplegarse, pero las pantallas que necesitan datos muestran el mensaje «El servicio de la clínica no está disponible». No intenta conectarse directamente a PostgreSQL ni ejecuta reglas de negocio localmente. Cuando el servicio vuelve a estar disponible, basta con recargar o reenviar la operación.

Si el servicio se reinicia, sus tokens de sesión se pierden. En ese caso hay que volver a iniciar sesión en el sitio.

## Configuración externa

Las direcciones no están fijadas en las clases Java. Para apuntar el cliente web a otro servidor, definir antes de iniciar Tomcat:

```text
CLINICA_SOAP_WSDL=http://otra-maquina:8080/clinica-servicio/soap/ClinicaService?wsdl
```

También se admite la propiedad de JVM `-Dclinica.soap.wsdl=...`. El valor local de desarrollo está en `clinica-web/src/main/webapp/WEB-INF/web.xml`, por lo que puede cambiarse como configuración de despliegue sin modificar ni recompilar Java.

El servicio admite estas variables para la base:

```text
CLINICA_DB_URL
CLINICA_DB_USER
CLINICA_DB_PASSWORD
```

Docker Compose ya las configura para la red de contenedores. Sin variables, la aplicación de escritorio utiliza los valores de `persistence.xml` (`localhost:5433`).

## Ejecutar la aplicación de escritorio

Con la base levantada:

```bash
mvn -pl clinica-core compile exec:java
```

El empaquetado también produce el JAR ejecutable del escritorio en `clinica-core/target`.

## Ciclo para cambiar el contrato SOAP

Después de agregar o modificar una operación en `ClinicaEndpoint`, ejecutar:

```bash
mvn clean package
```

La compilación vuelve a generar `ClinicaService.wsdl` y todas las clases cliente. No se deben copiar ni editar stubs a mano.

## Seguridad y sesiones

- El navegador mantiene la identidad en `HttpSession` mediante una cookie `HttpOnly`.
- El servicio devuelve un token opaco; la contraseña no se conserva en la sesión.
- `AuthFilter` centraliza la autorización de todas las rutas `/medico/*` y `/paciente/*`.
- El servicio vuelve a comprobar el rol asociado al token. Ocultar botones no es la medida de seguridad.
- Los precios de una solicitud se vuelven a consultar y validar en el servidor; no se aceptan precios enviados por el navegador.

## Convención para ramas

- Funcionalidades: `feature-nombre-descriptivo`
- Correcciones: `fix-nombre-del-error`
