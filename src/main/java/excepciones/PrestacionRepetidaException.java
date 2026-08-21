package excepciones;

public class PrestacionRepetidaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PrestacionRepetidaException(String mensaje) {
        super(mensaje);
    }
}
