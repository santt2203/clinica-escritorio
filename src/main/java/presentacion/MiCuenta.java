package presentacion;

import javax.swing.JLabel;

import datatypes.DtMedico;
import datatypes.DtPaciente;
import datatypes.DtUsuario;
import interfaces.IControlador;

/** Ventana interna con los datos principales del usuario actual. */
public class MiCuenta extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    public MiCuenta(IControlador icon, DtUsuario usuario) {
        super(icon, "Mi cuenta", 520, 220);

        agregarCampo("Email:", new JLabel(usuario.email()));
        agregarCampo("Nombre:", new JLabel(usuario.nombre()));

        String detalle = switch (usuario) {
            case DtMedico medico -> "Médico · " + medico.especialidad();
            case DtPaciente paciente -> "Paciente · " + paciente.mutualista();
        };

        agregarCampo("Rol:", new JLabel(detalle));
        agregarBoton("Cerrar", this::cerrar);
    }
}
