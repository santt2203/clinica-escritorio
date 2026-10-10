package web;

import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import cliente.soap.CantidadSoap;
import cliente.soap.ClinicaEndpoint;
import cliente.soap.ClinicaFaultException;
import cliente.soap.ClinicaService;
import cliente.soap.OrdenSoap;
import cliente.soap.Prestacion;
import cliente.soap.SeguidoSoap;
import cliente.soap.SesionSoap;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.BindingProvider;

/** Único adaptador del cliente web hacia las clases generadas por wsimport. */
public final class SoapClient {
    private static final String ATRIBUTO = SoapClient.class.getName();
    private static final QName SERVICIO = new QName("http://servicio.clinica/", "ClinicaService");

    private final URL wsdl;
    private final String endpoint;

    private SoapClient(ServletContext contexto) {
        try {
            String configurada = System.getenv("CLINICA_SOAP_WSDL");
            if (configurada == null || configurada.isBlank())
                configurada = System.getProperty("clinica.soap.wsdl");
            if ((configurada == null || configurada.isBlank()) && contexto != null)
                configurada = contexto.getInitParameter("clinica.soap.wsdl");
            if (configurada == null || configurada.isBlank())
                throw new IllegalStateException("Falta configurar clinica.soap.wsdl");
            wsdl = URI.create(configurada).toURL();
            endpoint = configurada.replaceFirst("(?i)\\?wsdl$", "");
        } catch (Exception e) {
            throw new IllegalStateException("La URL del servicio SOAP no es válida", e);
        }
    }

    public static SoapClient obtener(ServletContext contexto) {
        synchronized (contexto) {
            SoapClient cliente = (SoapClient) contexto.getAttribute(ATRIBUTO);
            if (cliente == null) {
                cliente = new SoapClient(contexto);
                contexto.setAttribute(ATRIBUTO, cliente);
            }
            return cliente;
        }
    }

    private ClinicaEndpoint puerto() {
        ClinicaEndpoint puerto = new ClinicaService(wsdl, SERVICIO).getClinicaPort();
        ((BindingProvider) puerto).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        return puerto;
    }

    private interface Llamada<T> { T invocar() throws ClinicaFaultException; }

    private <T> T llamar(Llamada<T> llamada) throws ServicioException {
        try {
            return llamada.invocar();
        } catch (ClinicaFaultException e) {
            String codigo = e.getFaultInfo() == null ? "ERROR_NEGOCIO" : e.getFaultInfo().getCodigo();
            String mensaje = e.getFaultInfo() == null ? e.getMessage() : e.getFaultInfo().getMensaje();
            throw new ServicioException(codigo, mensaje, e);
        } catch (RuntimeException e) {
            throw new ServicioException("SERVICIO_NO_DISPONIBLE",
                    "El servicio de la clínica no está disponible. Verificá que esté iniciado.", e);
        }
    }

    public void registrar(String tipo, String email, String nombre, String password, String detalle)
            throws ServicioException {
        llamar(() -> { puerto().registrarUsuario(tipo, email, nombre, password, detalle); return null; });
    }

    public SesionSoap iniciarSesion(String email, String password) throws ServicioException {
        return llamar(() -> puerto().iniciarSesion(email, password));
    }

    public void cerrarSesion(String token) {
        try { puerto().cerrarSesion(token); } catch (RuntimeException ignored) { }
    }

    public List<Prestacion> catalogo(String texto) throws ServicioException {
        return texto == null || texto.isBlank()
                ? llamar(() -> puerto().listarCatalogo())
                : llamar(() -> puerto().buscarPrestaciones(texto));
    }

    public Prestacion prestacion(long id) throws ServicioException {
        return llamar(() -> puerto().obtenerPrestacion(id));
    }

    public void agregarPrestacion(String token, Prestacion prestacion) throws ServicioException {
        llamar(() -> { puerto().agregarPrestacion(token, prestacion); return null; });
    }

    public void modificarPrestacion(String token, Prestacion prestacion) throws ServicioException {
        llamar(() -> { puerto().modificarPrestacion(token, prestacion); return null; });
    }

    public void eliminarPrestacion(String token, long id) throws ServicioException {
        llamar(() -> { puerto().eliminarPrestacion(token, id); return null; });
    }

    public List<SeguidoSoap> seguidos(String token) throws ServicioException {
        return llamar(() -> puerto().listarSeguidos(token));
    }

    public void agregarSeguido(String token, long id) throws ServicioException {
        llamar(() -> { puerto().agregarSeguido(token, id); return null; });
    }

    public void quitarSeguido(String token, long id) throws ServicioException {
        llamar(() -> { puerto().quitarSeguido(token, id); return null; });
    }

    public void confirmarOrden(String token, List<CantidadSoap> lineas) throws ServicioException {
        llamar(() -> { puerto().confirmarOrden(token, lineas); return null; });
    }

    public List<OrdenSoap> ordenes(String token) throws ServicioException {
        return llamar(() -> puerto().listarOrdenes(token));
    }
}
