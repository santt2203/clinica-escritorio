package servicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import datatypes.DtEstudio;
import datatypes.DtLineaOrden;
import datatypes.DtMedico;
import datatypes.DtOrden;
import datatypes.DtPaciente;
import datatypes.DtPrestacion;
import datatypes.DtSeguido;
import datatypes.DtTerapia;
import datatypes.DtUsuario;
import datatypes.Franja;
import interfaces.Fabrica;
import interfaces.IControlador;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import servicio.dto.CantidadSoap;
import servicio.dto.LineaOrdenSoap;
import servicio.dto.OrdenSoap;
import servicio.dto.PrestacionSoap;
import servicio.dto.SeguidoSoap;
import servicio.dto.SesionSoap;
import servicio.dto.UsuarioSoap;

@WebService(serviceName = "ClinicaService", portName = "ClinicaPort",
        targetNamespace = "http://servicio.clinica/")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public class ClinicaEndpoint {

    private static final Object DOMINIO_LOCK = new Object();
    private static final Map<String, SesionInterna> SESIONES = new ConcurrentHashMap<>();

    private record SesionInterna(String email, String rol) { }

    @FunctionalInterface
    private interface Operacion<T> { T ejecutar() throws Exception; }

    private static <T> T ejecutar(Operacion<T> operacion) throws ClinicaFaultException {
        synchronized (DOMINIO_LOCK) {
            try {
                return operacion.ejecutar();
            } catch (ClinicaFaultException e) {
                throw e;
            } catch (Exception e) {
                String mensaje = e.getMessage() == null ? "No se pudo completar la operación" : e.getMessage();
                throw new ClinicaFaultException(mensaje,
                        new ClinicaFaultInfo(e.getClass().getSimpleName(), mensaje), e);
            }
        }
    }

    @WebMethod
    public void registrarUsuario(
            @WebParam(name = "tipo") String tipo,
            @WebParam(name = "email") String email,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "password") String password,
            @WebParam(name = "detalle") String detalle) throws ClinicaFaultException {
        ejecutar(() -> {
            IControlador controlador = Fabrica.getInstancia().getIControlador();
            String tipoNormalizado = requerido(tipo, "El tipo de usuario es obligatorio").toUpperCase(Locale.ROOT);
            if ("MEDICO".equals(tipoNormalizado))
                controlador.registrarMedico(email, nombre, password, detalle);
            else if ("PACIENTE".equals(tipoNormalizado))
                controlador.registrarPaciente(email, nombre, password, detalle);
            else
                throw new IllegalArgumentException("El tipo de usuario debe ser MÉDICO o PACIENTE");
            return null;
        });
    }

    @WebMethod
    @WebResult(name = "sesion")
    public SesionSoap iniciarSesion(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password) throws ClinicaFaultException {
        return ejecutar(() -> {
            DtUsuario usuario = Fabrica.getInstancia().getIControlador().iniciarSesion(email, password);
            String rol = usuario instanceof DtMedico ? "MEDICO" : "PACIENTE";
            String token = UUID.randomUUID().toString();
            SESIONES.put(token, new SesionInterna(usuario.email(), rol));
            SesionSoap respuesta = new SesionSoap();
            respuesta.setToken(token);
            respuesta.setUsuario(aSoap(usuario));
            return respuesta;
        });
    }

    @WebMethod
    public void cerrarSesion(@WebParam(name = "token") String token) {
        if (token != null)
            SESIONES.remove(token);
    }

    @WebMethod
    public void agregarPrestacion(
            @WebParam(name = "token") String token,
            @WebParam(name = "prestacion") PrestacionSoap prestacion) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "MEDICO");
            controladorPara(sesion).agregarPrestacion(aDominio(prestacion, false));
            return null;
        });
    }

    @WebMethod
    public void modificarPrestacion(
            @WebParam(name = "token") String token,
            @WebParam(name = "prestacion") PrestacionSoap prestacion) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "MEDICO");
            controladorPara(sesion).modificarPrestacion(aDominio(prestacion, true));
            return null;
        });
    }

    @WebMethod
    public void eliminarPrestacion(
            @WebParam(name = "token") String token,
            @WebParam(name = "idPrestacion") Long idPrestacion) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "MEDICO");
            controladorPara(sesion).eliminarPrestacion(idPrestacion);
            return null;
        });
    }

    @WebMethod
    @WebResult(name = "prestacion")
    public List<PrestacionSoap> listarCatalogo() throws ClinicaFaultException {
        return ejecutar(() -> Fabrica.getInstancia().getIControlador().listarCatalogo().stream()
                .map(ClinicaEndpoint::aSoap).toList());
    }

    @WebMethod
    @WebResult(name = "prestacion")
    public List<PrestacionSoap> buscarPrestaciones(
            @WebParam(name = "texto") String texto) throws ClinicaFaultException {
        return ejecutar(() -> Fabrica.getInstancia().getIControlador().buscarPrestaciones(texto).stream()
                .map(ClinicaEndpoint::aSoap).toList());
    }

    @WebMethod
    @WebResult(name = "prestacion")
    public PrestacionSoap obtenerPrestacion(
            @WebParam(name = "idPrestacion") Long idPrestacion) throws ClinicaFaultException {
        return ejecutar(() -> aSoap(Fabrica.getInstancia().getIControlador().obtenerPrestacion(idPrestacion)));
    }

    @WebMethod
    public void agregarSeguido(
            @WebParam(name = "token") String token,
            @WebParam(name = "idPrestacion") Long idPrestacion) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "PACIENTE");
            controladorPara(sesion).agregarSeguido(sesion.email(), idPrestacion);
            return null;
        });
    }

    @WebMethod
    public void quitarSeguido(
            @WebParam(name = "token") String token,
            @WebParam(name = "idPrestacion") Long idPrestacion) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "PACIENTE");
            controladorPara(sesion).quitarSeguido(sesion.email(), idPrestacion);
            return null;
        });
    }

    @WebMethod
    @WebResult(name = "seguido")
    public List<SeguidoSoap> listarSeguidos(
            @WebParam(name = "token") String token) throws ClinicaFaultException {
        return ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "PACIENTE");
            return controladorPara(sesion).listarSeguidos(sesion.email()).stream()
                    .map(ClinicaEndpoint::aSoap).toList();
        });
    }

    @WebMethod
    public void confirmarOrden(
            @WebParam(name = "token") String token,
            @WebParam(name = "linea") List<CantidadSoap> lineas) throws ClinicaFaultException {
        ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "PACIENTE");
            Map<Long, Integer> cantidades = new LinkedHashMap<>();
            if (lineas != null)
                for (CantidadSoap linea : lineas) {
                    if (linea == null || linea.getIdPrestacion() == null)
                        throw new IllegalArgumentException("Cada línea debe indicar una prestación");
                    cantidades.merge(linea.getIdPrestacion(), linea.getCantidad(), Integer::sum);
                }
            controladorPara(sesion).confirmarOrden(sesion.email(), cantidades);
            return null;
        });
    }

    @WebMethod
    @WebResult(name = "orden")
    public List<OrdenSoap> listarOrdenes(
            @WebParam(name = "token") String token) throws ClinicaFaultException {
        return ejecutar(() -> {
            SesionInterna sesion = requerirSesion(token, "PACIENTE");
            return controladorPara(sesion).listarOrdenes(sesion.email()).stream()
                    .map(ClinicaEndpoint::aSoap).toList();
        });
    }

    private static IControlador controladorPara(SesionInterna sesion) {
        return Fabrica.getInstancia().getIControladorParaSesion(sesion.email());
    }

    private static SesionInterna requerirSesion(String token, String rol) throws ClinicaFaultException {
        SesionInterna sesion = token == null ? null : SESIONES.get(token);
        if (sesion == null)
            throw new ClinicaFaultException("La sesión no existe o venció",
                    new ClinicaFaultInfo("SESION_INVALIDA", "La sesión no existe o venció"));
        if (!rol.equals(sesion.rol()))
            throw new ClinicaFaultException("No tenés permiso para realizar esta operación",
                    new ClinicaFaultInfo("ACCESO_DENEGADO", "No tenés permiso para realizar esta operación"));
        return sesion;
    }

    private static String requerido(String valor, String mensaje) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException(mensaje);
        return valor.trim();
    }

    private static DtPrestacion aDominio(PrestacionSoap p, boolean requiereId) {
        if (p == null)
            throw new IllegalArgumentException("La prestación es obligatoria");
        if (requiereId && p.getId() == null)
            throw new IllegalArgumentException("El identificador es obligatorio");
        Franja franja;
        try {
            franja = Franja.valueOf(requerido(p.getFranja(), "La franja es obligatoria").toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La franja debe ser MAÑANA, TARDE o NOCHE");
        }
        String tipo = requerido(p.getTipo(), "El tipo de prestación es obligatorio").toUpperCase(Locale.ROOT);
        return switch (tipo) {
            case "ESTUDIO" -> new DtEstudio(p.getId(), p.getNombre(), p.getPrecio(), franja,
                    p.getDuracionMinutos() == null ? 0 : p.getDuracionMinutos());
            case "TERAPIA" -> new DtTerapia(p.getId(), p.getNombre(), p.getPrecio(), franja,
                    Boolean.TRUE.equals(p.getRequiereDerivacion()),
                    p.getCantidadSesiones() == null ? 0 : p.getCantidadSesiones());
            default -> throw new IllegalArgumentException("El tipo de prestación debe ser ESTUDIO o TERAPIA");
        };
    }

    private static PrestacionSoap aSoap(DtPrestacion p) {
        PrestacionSoap dto = new PrestacionSoap();
        dto.setId(p.id()); dto.setNombre(p.nombre()); dto.setPrecio(p.precio()); dto.setFranja(p.franja().name());
        if (p instanceof DtEstudio estudio) {
            dto.setTipo("ESTUDIO"); dto.setDuracionMinutos(estudio.duracionMinutos());
        } else if (p instanceof DtTerapia terapia) {
            dto.setTipo("TERAPIA"); dto.setRequiereDerivacion(terapia.requiereDerivacion());
            dto.setCantidadSesiones(terapia.cantidadSesiones());
        }
        return dto;
    }

    private static UsuarioSoap aSoap(DtUsuario u) {
        UsuarioSoap dto = new UsuarioSoap();
        dto.setEmail(u.email()); dto.setNombre(u.nombre());
        if (u instanceof DtMedico medico) {
            dto.setTipo("MEDICO"); dto.setDetalle(medico.especialidad());
        } else if (u instanceof DtPaciente paciente) {
            dto.setTipo("PACIENTE"); dto.setDetalle(paciente.mutualista());
        }
        return dto;
    }

    private static SeguidoSoap aSoap(DtSeguido seguido) {
        SeguidoSoap dto = new SeguidoSoap();
        dto.setPrestacion(aSoap(seguido.prestacion())); dto.setFecha(seguido.fecha().toString());
        return dto;
    }

    private static OrdenSoap aSoap(DtOrden orden) {
        OrdenSoap dto = new OrdenSoap();
        dto.setId(orden.id()); dto.setFecha(orden.fecha().toString()); dto.setTotal(orden.total());
        dto.setLineas(orden.lineas().stream().map(ClinicaEndpoint::aSoap).toList());
        return dto;
    }

    private static LineaOrdenSoap aSoap(DtLineaOrden linea) {
        LineaOrdenSoap dto = new LineaOrdenSoap();
        dto.setNombrePrestacion(linea.nombrePrestacion()); dto.setCantidad(linea.cantidad());
        dto.setPrecioUnitario(linea.precioUnitario()); dto.setSubtotal(linea.subtotal());
        return dto;
    }
}
