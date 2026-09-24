package excepciones;

public class PrestacionEnOrdenException extends Exception {

    private static final long serialVersionUID = 1L;

    public PrestacionEnOrdenException(String mensaje) {
        super(mensaje);
    }
}
