package logica;

import java.util.List;

import jakarta.persistence.EntityManager;
import persistencia.Conexion;

public class ManejadorUsuario {

    private static ManejadorUsuario instancia = null;

    private ManejadorUsuario() {
    }

    public static ManejadorUsuario getInstancia() {
        if (instancia == null)
            instancia = new ManejadorUsuario();
        return instancia;
    }

    public void agregarUsuario(Usuario usuario) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    public Usuario buscarUsuario(String email) {
        return Conexion.getInstancia().getEntityManager().find(Usuario.class, email);
    }

    public List<String> listarEmails() {
        return Conexion.getInstancia().getEntityManager()
                .createQuery("select u.email from Usuario u order by u.email", String.class)
                .getResultList();
    }
}
