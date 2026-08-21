package logica;

import datatypes.DtPaciente;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    private String mutualista;

    public Paciente() {
    }

    public Paciente(String email, String nombre, String password, String mutualista) {
        super(email, nombre, password);
        this.mutualista = mutualista;
    }

    public String getMutualista() { return mutualista; }

    public void setMutualista(String mutualista) { this.mutualista = mutualista; }

    @Override
    public DtPaciente getDtUsuario() {
        return new DtPaciente(getEmail(), getNombre(), mutualista);
    }
}
