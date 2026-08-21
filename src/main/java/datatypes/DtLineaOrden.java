package datatypes;

public record DtLineaOrden(String nombrePrestacion, int cantidad, double precioUnitario) {

    public double subtotal() {
        return cantidad * precioUnitario;
    }
}
