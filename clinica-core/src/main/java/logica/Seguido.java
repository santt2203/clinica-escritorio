package logica;

import java.time.LocalDate;

import datatypes.DtSeguido;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Clase asociativa de la relacion muchos-a-muchos Paciente <-> Prestacion.
 * Es una entidad normal porque la relacion tiene su propio atributo: la fecha.
 * El @UniqueConstraint es lo que impide que un paciente marque dos veces la misma prestacion.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "paciente_email", "prestacion_id" }))
public class Seguido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private Prestacion prestacion;

    private LocalDate fecha;

    public Seguido() {
    }

    public Seguido(Paciente paciente, Prestacion prestacion) {
        this.paciente = paciente;
        this.prestacion = prestacion;
        this.fecha = LocalDate.now();
    }

    public Prestacion getPrestacion() { return prestacion; }

    public LocalDate getFecha() { return fecha; }

    public DtSeguido getDtSeguido() {
        return new DtSeguido(prestacion.getDtPrestacion(), fecha);
    }
}
