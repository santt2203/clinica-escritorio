package persistencia;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * 
 * : en toda la aplicacion existe una sola Conexion.
 * "clinica" es el nombre de la persistence-unit de META-INF/persistence.xml.
 */
public class Conexion {

    private static Conexion instancia = null;

    private final EntityManagerFactory emf;
    private final EntityManager em;

    private Conexion() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Map<String, String> propiedades = new HashMap<>();
        agregarSiExiste(propiedades, "jakarta.persistence.jdbc.url", "CLINICA_DB_URL");
        agregarSiExiste(propiedades, "jakarta.persistence.jdbc.user", "CLINICA_DB_USER");
        agregarSiExiste(propiedades, "jakarta.persistence.jdbc.password", "CLINICA_DB_PASSWORD");
        emf = Persistence.createEntityManagerFactory("clinica", propiedades);
        em = emf.createEntityManager();
    }

    private static void agregarSiExiste(Map<String, String> propiedades, String propiedad, String variable) {
        String valor = System.getenv(variable);
        if (valor != null && !valor.isBlank())
            propiedades.put(propiedad, valor);
    }

    public static Conexion getInstancia() {
        if (instancia == null)
            instancia = new Conexion();
        return instancia;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}
