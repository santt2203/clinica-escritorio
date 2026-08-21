package excepciones;

public class OrdenVaciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public OrdenVaciaException(String mensaje) {
        super(mensaje);
    }
}
