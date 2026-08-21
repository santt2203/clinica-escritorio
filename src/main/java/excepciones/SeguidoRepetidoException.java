package excepciones;

public class SeguidoRepetidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public SeguidoRepetidoException(String mensaje) {
        super(mensaje);
    }
}
