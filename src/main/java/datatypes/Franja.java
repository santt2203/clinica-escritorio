package datatypes;

public enum Franja {

    MANANA("Mañana"),
    TARDE("Tarde"),
    NOCHE("Noche");

    private final String texto;

    Franja(String texto) {
        this.texto = texto;
    }

    /**
     * Los combos y las tablas de Swing muestran lo que devuelve toString(), así que con
     * sobreescribirlo alcanza para que en pantalla se lea "Mañana" y no "MANANA".
     *
     * En la base de datos y en el servicio web sigue viajando el nombre de la constante,
     * porque tanto Hibernate como JAXB usan name() y no toString(). El texto es solo para
     * mostrar.
     */
    @Override
    public String toString() {
        return texto;
    }
}
