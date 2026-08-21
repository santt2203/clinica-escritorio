package logica;

import java.util.List;

import jakarta.persistence.EntityManager;
import persistencia.Conexion;

public class ManejadorPrestacion {

    private static ManejadorPrestacion instancia = null;

    private ManejadorPrestacion() {
    }

    public static ManejadorPrestacion getInstancia() {
        if (instancia == null)
            instancia = new ManejadorPrestacion();
        return instancia;
    }

    public void agregarPrestacion(Prestacion prestacion) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.persist(prestacion);
        em.getTransaction().commit();
    }

    public void actualizarPrestacion(Prestacion prestacion) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.merge(prestacion);
        em.getTransaction().commit();
    }

    /** Antes de borrar la prestacion hay que borrar los seguidos que la apuntan. */
    public void eliminarPrestacion(Prestacion prestacion) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from Seguido s where s.prestacion = :prestacion")
                .setParameter("prestacion", prestacion)
                .executeUpdate();
        em.remove(prestacion);
        em.getTransaction().commit();
    }

    public Prestacion buscarPrestacion(Long id) {
        return Conexion.getInstancia().getEntityManager().find(Prestacion.class, id);
    }

    public Prestacion buscarPrestacionPorNombre(String nombre) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery("select p from Prestacion p where p.nombre = :nombre", Prestacion.class)
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Prestacion> listarPrestaciones() {
        return Conexion.getInstancia().getEntityManager()
                .createQuery("select p from Prestacion p order by p.nombre", Prestacion.class)
                .getResultList();
    }

    /**
     * Busca las prestaciones cuyo nombre contiene el texto, sin distinguir mayúsculas.
     * El "%" a los dos lados es lo que hace que alcance con una parte del nombre.
     */
    public List<Prestacion> buscarPrestaciones(String texto) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery(
                        "select p from Prestacion p where lower(p.nombre) like :texto order by p.nombre",
                        Prestacion.class)
                .setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }

    /** Una prestacion que ya aparece en una orden medica no se puede borrar. */
    public boolean fueOrdenada(Prestacion prestacion) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery("select count(l) from LineaOrden l where l.prestacion = :prestacion", Long.class)
                .setParameter("prestacion", prestacion)
                .getSingleResult() > 0;
    }
}
