package logica;

import datatypes.DtLineaOrden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "orden_id", "prestacion_id" }))
public class LineaOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrdenMedica orden;

    @ManyToOne
    private Prestacion prestacion;

    private int cantidad;

    /**
     * Copia del precio al momento de emitir la orden. Si mañana el medico cambia el precio
     * de la prestacion, las ordenes ya hechas siguen mostrando lo que se cobro.
     */
    private double precioUnitario;

    public LineaOrden() {
    }

    public LineaOrden(OrdenMedica orden, Prestacion prestacion, int cantidad) {
        this.orden = orden;
        this.prestacion = prestacion;
        this.cantidad = cantidad;
        this.precioUnitario = prestacion.getPrecio();
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    public DtLineaOrden getDtLineaOrden() {
        return new DtLineaOrden(prestacion.getNombre(), cantidad, precioUnitario);
    }
}
