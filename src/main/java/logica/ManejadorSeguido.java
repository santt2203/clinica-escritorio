package logica;

import java.util.List;

import jakarta.persistence.EntityManager;
import persistencia.Conexion;

public class ManejadorSeguido {

    private static ManejadorSeguido instancia = null;

    private ManejadorSeguido() {
    }

    public static ManejadorSeguido getInstancia() {
        if (instancia == null)
            instancia = new ManejadorSeguido();
        return instancia;
    }

    public void agregarSeguido(Seguido seguido) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.persist(seguido);
        em.getTransaction().commit();
    }

    public void eliminarSeguido(Seguido seguido) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.remove(seguido);
        em.getTransaction().commit();
    }

    public Seguido buscarSeguido(Paciente paciente, Prestacion prestacion) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery(
                        "select s from Seguido s where s.paciente = :paciente and s.prestacion = :prestacion",
                        Seguido.class)
                .setParameter("paciente", paciente)
                .setParameter("prestacion", prestacion)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Seguido> listarSeguidos(Paciente paciente) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery(
                        "select s from Seguido s where s.paciente = :paciente order by s.prestacion.nombre",
                        Seguido.class)
                .setParameter("paciente", paciente)
                .getResultList();
    }
}
