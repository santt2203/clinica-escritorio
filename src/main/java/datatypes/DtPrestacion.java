package datatypes;

/**
 * Espejo, en la capa de datatypes, de la herencia Prestacion -> Estudio / Terapia.
 */
public sealed interface DtPrestacion permits DtEstudio, DtTerapia {

    Long id();

    String nombre();

    double precio();

    Franja franja();
}
