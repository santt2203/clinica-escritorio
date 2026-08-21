package datatypes;

public record DtTerapia(Long id, String nombre, double precio, Franja franja,
        boolean requiereDerivacion, int cantidadSesiones) implements DtPrestacion {
}
