package servicio.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "prestacion", propOrder = { "id", "tipo", "nombre", "precio", "franja",
        "duracionMinutos", "requiereDerivacion", "cantidadSesiones" })
public class PrestacionSoap {
    private Long id;
    private String tipo;
    private String nombre;
    private double precio;
    private String franja;
    private Integer duracionMinutos;
    private Boolean requiereDerivacion;
    private Integer cantidadSesiones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getFranja() { return franja; }
    public void setFranja(String franja) { this.franja = franja; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public Boolean getRequiereDerivacion() { return requiereDerivacion; }
    public void setRequiereDerivacion(Boolean requiereDerivacion) { this.requiereDerivacion = requiereDerivacion; }
    public Integer getCantidadSesiones() { return cantidadSesiones; }
    public void setCantidadSesiones(Integer cantidadSesiones) { this.cantidadSesiones = cantidadSesiones; }
}
