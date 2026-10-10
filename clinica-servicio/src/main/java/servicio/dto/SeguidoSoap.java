package servicio.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SeguidoSoap {
    private PrestacionSoap prestacion;
    private String fecha;

    public PrestacionSoap getPrestacion() { return prestacion; }
    public void setPrestacion(PrestacionSoap prestacion) { this.prestacion = prestacion; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
