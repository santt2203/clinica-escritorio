package logica;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import datatypes.DtOrden;
import datatypes.DtPrestacion;
import datatypes.DtSeguido;
import datatypes.DtUsuario;
import excepciones.CredencialesInvalidasException;
import excepciones.OrdenVaciaException;
import excepciones.PrestacionEnOrdenException;
import excepciones.PrestacionRepetidaException;
import excepciones.SeguidoNoExisteException;
import excepciones.SeguidoRepetidoException;
import excepciones.UsuarioRepetidoException;
import interfaces.IControlador;

public class Controlador implements IControlador {

    @Override
    public void registrarMedico(String email, String nombre, String password, String especialidad)
            throws UsuarioRepetidoException {
        verificarEmailLibre(email);
        ManejadorUsuario.getInstancia()
                .agregarUsuario(new Medico(email, nombre, hash(password), especialidad));
    }

    @Override
    public void registrarPaciente(String email, String nombre, String password, String mutualista)
            throws UsuarioRepetidoException {
        verificarEmailLibre(email);
        ManejadorUsuario.getInstancia()
                .agregarUsuario(new Paciente(email, nombre, hash(password), mutualista));
    }

    @Override
    public DtUsuario iniciarSesion(String email, String password) throws CredencialesInvalidasException {
        Usuario usuario = ManejadorUsuario.getInstancia().buscarUsuario(email);
        if (usuario == null || !usuario.getPassword().equals(hash(password)))
            throw new CredencialesInvalidasException("Email o contraseña incorrectos");
        return usuario.getDtUsuario();
    }

    private void verificarEmailLibre(String email) throws UsuarioRepetidoException {
        if (ManejadorUsuario.getInstancia().buscarUsuario(email) != null)
            throw new UsuarioRepetidoException("Ya existe un usuario con el email " + email);
    }

    /** Nunca guardamos la contraseña tal cual: guardamos su huella SHA-256. */
    private static String hash(String texto) {
        try {
            MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(algoritmo.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void agregarPrestacion(DtPrestacion prestacion) throws PrestacionRepetidaException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public void modificarPrestacion(DtPrestacion prestacion) throws PrestacionRepetidaException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public void eliminarPrestacion(Long idPrestacion) throws PrestacionEnOrdenException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public List<DtPrestacion> listarCatalogo() {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public List<DtPrestacion> buscarPrestaciones(String texto) {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public DtPrestacion obtenerPrestacion(Long idPrestacion) {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public void agregarSeguido(String email, Long idPrestacion) throws SeguidoRepetidoException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public void quitarSeguido(String email, Long idPrestacion) throws SeguidoNoExisteException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public List<DtSeguido> listarSeguidos(String email) {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public void confirmarOrden(String email, Map<Long, Integer> cantidadPorPrestacion)
            throws OrdenVaciaException {
        throw new UnsupportedOperationException("Pendiente");
    }

    @Override
    public List<DtOrden> listarOrdenes(String email) {
        throw new UnsupportedOperationException("Pendiente");
    }
}
