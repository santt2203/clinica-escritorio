package web;

public class ServicioException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String codigo;

    public ServicioException(String codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    public String getCodigo() { return codigo; }
}
