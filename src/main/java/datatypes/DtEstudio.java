package datatypes;

public record DtEstudio(Long id, String nombre, double precio, Franja franja, int duracionMinutos)
        implements DtPrestacion {
}
