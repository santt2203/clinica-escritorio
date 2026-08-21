package excepciones;

public class UsuarioRepetidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsuarioRepetidoException(String mensaje) {
        super(mensaje);
    }
}
