package servicio.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class LineaOrdenSoap {
    private String nombrePrestacion;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public String getNombrePrestacion() { return nombrePrestacion; }
    public void setNombrePrestacion(String nombrePrestacion) { this.nombrePrestacion = nombrePrestacion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
