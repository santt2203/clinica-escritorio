package servicio;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ClinicaFaultInfo {
    private String codigo;
    private String mensaje;

    public ClinicaFaultInfo() { }
    public ClinicaFaultInfo(String codigo, String mensaje) { this.codigo = codigo; this.mensaje = mensaje; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
