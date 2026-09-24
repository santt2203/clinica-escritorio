package servicio.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SesionSoap {
    private String token;
    private UsuarioSoap usuario;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UsuarioSoap getUsuario() { return usuario; }
    public void setUsuario(UsuarioSoap usuario) { this.usuario = usuario; }
}
