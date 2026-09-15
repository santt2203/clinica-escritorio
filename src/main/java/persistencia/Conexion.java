package persistencia;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton: en toda la aplicacion existe una sola Conexion.
 * "clinica" es el nombre de la persistence-unit de META-INF/persistence.xml.
 */
public class Conexion {

    private static Conexion instancia = null;

    private final EntityManagerFactory emf;
    private final EntityManager em;

    private Conexion() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        emf = Persistence.createEntityManagerFactory("clinica");
        em = emf.createEntityManager();
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
