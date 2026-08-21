package logica;

import datatypes.DtPrestacion;
import datatypes.Franja;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Prestacion {

    /** IDENTITY: el id lo genera PostgreSQL, no lo escribe el usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double precio;

    /** STRING guarda "MANANA" en la tabla. Sin esto Hibernate guardaria 0, 1, 2. */
    @Enumerated(EnumType.STRING)
    private Franja franja;

    public Prestacion() {
    }

    public Prestacion(String nombre, double precio, Franja franja) {
        this.nombre = nombre;
        this.precio = precio;
        this.franja = franja;
    }

    public abstract DtPrestacion getDtPrestacion();

    public Long getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) { this.precio = precio; }

    public Franja getFranja() { return franja; }

    public void setFranja(Franja franja) { this.franja = franja; }
}
