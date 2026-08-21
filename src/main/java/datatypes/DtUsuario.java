package datatypes;

/**
 * "sealed" declara que los unicos tipos de usuario posibles son estos dos.
 * Es el espejo, en la capa de datatypes, de la herencia Usuario -> Medico / Paciente.
 */
public sealed interface DtUsuario permits DtMedico, DtPaciente {

    String email();

    String nombre();
}
