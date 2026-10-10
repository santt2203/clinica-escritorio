package web;

import cliente.soap.Prestacion;

public class SolicitudItem {
    private final Prestacion prestacion;
    private final int cantidad;
    public SolicitudItem(Prestacion prestacion, int cantidad) { this.prestacion = prestacion; this.cantidad = cantidad; }
    public Prestacion getPrestacion() { return prestacion; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return cantidad * prestacion.getPrecio(); }
}
