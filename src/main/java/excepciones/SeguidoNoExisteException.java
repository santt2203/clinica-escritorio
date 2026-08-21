package excepciones;

public class SeguidoNoExisteException extends Exception {

    private static final long serialVersionUID = 1L;

    public SeguidoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
