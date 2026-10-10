package excepciones;

public class AccesoDenegadoException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
