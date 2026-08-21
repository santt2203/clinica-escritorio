package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import datatypes.DtOrden;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Una orden medica con sus lineas es un todo: se arma completa y se guarda de una sola vez.
 * Por eso aca SI usamos cascade = ALL (al persistir la OrdenMedica se persisten sus LineaOrden)
 * y orphanRemoval = true (si se saca una linea de la lista, se borra de la tabla).
 */
@Entity
public class OrdenMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;

    private LocalDateTime fecha;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaOrden> lineas = new ArrayList<>();

    public OrdenMedica() {
    }

    public OrdenMedica(Paciente paciente) {
        this(paciente, LocalDateTime.now());
    }

    /** Con fecha explícita: lo usa la carga de datos de ejemplo, para que el historial
     *  tenga ordenes de distintos días y se note el orden. */
    public OrdenMedica(Paciente paciente, LocalDateTime fecha) {
        this.paciente = paciente;
        this.fecha = fecha;
    }

    public Long getId() { return id; }

    public LocalDateTime getFecha() { return fecha; }

    public void agregarLinea(Prestacion prestacion, int cantidad) {
        lineas.add(new LineaOrden(this, prestacion, cantidad));
    }

    public double getTotal() {
        return lineas.stream().mapToDouble(LineaOrden::getSubtotal).sum();
    }

    public DtOrden getDtOrden() {
        return new DtOrden(id, fecha, lineas.stream().map(LineaOrden::getDtLineaOrden).toList());
    }
}
