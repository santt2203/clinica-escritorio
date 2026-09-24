package servicio.dto;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrdenSoap {
    private Long id;
    private String fecha;
    private List<LineaOrdenSoap> lineas = new ArrayList<>();
    private double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public List<LineaOrdenSoap> getLineas() { return lineas; }
    public void setLineas(List<LineaOrdenSoap> lineas) { this.lineas = lineas; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
