package servicio.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CantidadSoap {
    private Long idPrestacion;
    private int cantidad;

    public Long getIdPrestacion() { return idPrestacion; }
    public void setIdPrestacion(Long idPrestacion) { this.idPrestacion = idPrestacion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
