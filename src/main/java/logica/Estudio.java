package logica;

import datatypes.DtEstudio;
import datatypes.Franja;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTUDIO")
public class Estudio extends Prestacion {

    private int duracionMinutos;

    public Estudio() {
    }

    public Estudio(String nombre, double precio, Franja franja, int duracionMinutos) {
        super(nombre, precio, franja);
        this.duracionMinutos = duracionMinutos;
    }

    public int getDuracionMinutos() { return duracionMinutos; }

    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    @Override
    public DtEstudio getDtPrestacion() {
        return new DtEstudio(getId(), getNombre(), getPrecio(), getFranja(), duracionMinutos);
    }
}
