# Clínica — aplicación de escritorio

Java 21 · Hibernate 6 · PostgreSQL · Swing.

Arquitectura: `presentacion → interfaces → logica → persistencia`

La presentación no conoce las entidades: usa `IControlador` y los datatypes. El controlador se pide a `Fabrica`, no con `new`.

## Casos de uso obligatorios

| # | Caso de uso | Quién | Estado |
|---|---|---|---|
| 1 | Registrar un usuario, que puede ser médico o paciente | Cualquiera | Implementado |
| 2 | Iniciar sesión. Las contraseñas no se guardan en texto plano | Cualquiera | Implementado |
| 3 | Agregar una prestación al catálogo | Médico | Implementado |
| 4 | Modificar una prestación | Médico | Pendiente |
| 5 | Eliminar una prestación | Médico | Implementado |
| 6 | Ver el catálogo de prestaciones | Cualquiera | Implementado |

El inicio de sesión utiliza hashes PBKDF2 con una sal aleatoria por usuario.

## Diagrama de clases

![Diagrama de clases de la clínica](diagrama-clases-clinica.png)

## Cómo ejecutar

Requisitos: JDK 21, Maven 3.9 o mayor y Docker Desktop (con el motor en marcha).

Docker publica PostgreSQL en el puerto `5433` para evitar conflictos con instalaciones locales de PostgreSQL que suelen utilizar el puerto `5432`.

```bash
docker compose up -d
mvn compile exec:java
```

`mvn exec:java` no recompila. Si cambiaste código, usá `mvn compile exec:java`.

Apagar la base: `docker compose down`.  
Borrar los datos y empezar de cero: `docker compose down -v`.

## Solución de problemas de PostgreSQL

Si la aplicación informa que falló la autenticación del usuario `postgres`, es posible que el volumen `clinica-datos` haya sido creado anteriormente con otra contraseña. Las variables de `docker-compose.yml` se aplican solamente la primera vez que PostgreSQL inicializa el volumen; al reutilizar un volumen existente, no se actualizan las credenciales.

Si la conexión sigue fallando y tenés PostgreSQL instalado en Windows, verificá que la aplicación use el puerto `5433`. El puerto `5432` puede estar siendo atendido por la instalación local en lugar del contenedor Docker.

Para recrear la base con las credenciales actuales (`postgres` / `lapass`):

```bash
docker compose down -v
docker compose up -d
```

Este procedimiento elimina el volumen y todos los datos locales de la base. Usalo únicamente si no necesitás conservarlos.

## Convención para ramas

Las ramas deben seguir estas convenciones:

- Nuevas funcionalidades: `feature-nombre-descriptivo`
- Correcciones: `fix-nombre-del-error`

Se deben usar minúsculas y separar las palabras con guiones. Por ejemplo:

```text
feature-gestion-usuarios
fix-error-inicio-sesion
```
