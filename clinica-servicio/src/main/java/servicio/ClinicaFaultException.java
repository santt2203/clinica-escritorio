package servicio;

import jakarta.xml.ws.WebFault;

@WebFault(name = "ClinicaFault", targetNamespace = "http://servicio.clinica/")
public class ClinicaFaultException extends Exception {
    private static final long serialVersionUID = 1L;
    private final ClinicaFaultInfo faultInfo;

    public ClinicaFaultException(String message, ClinicaFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public ClinicaFaultException(String message, ClinicaFaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public ClinicaFaultInfo getFaultInfo() { return faultInfo; }
}
