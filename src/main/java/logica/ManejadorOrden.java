package logica;

import java.util.List;

import jakarta.persistence.EntityManager;
import persistencia.Conexion;

public class ManejadorOrden {

    private static ManejadorOrden instancia = null;

    private ManejadorOrden() {
    }

    public static ManejadorOrden getInstancia() {
        if (instancia == null)
            instancia = new ManejadorOrden();
        return instancia;
    }

    /** Al persistir la OrdenMedica, la cascada persiste tambien sus LineaOrden. */
    public void agregarOrden(OrdenMedica orden) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        em.getTransaction().begin();
        em.persist(orden);
        em.getTransaction().commit();
    }

    /** El "order by fecha desc" es el "mas reciente primero" que pide la letra. */
    public List<OrdenMedica> listarOrdenes(Paciente paciente) {
        return Conexion.getInstancia().getEntityManager()
                .createQuery(
                        "select o from OrdenMedica o where o.paciente = :paciente order by o.fecha desc",
                        OrdenMedica.class)
                .setParameter("paciente", paciente)
                .getResultList();
    }
}
