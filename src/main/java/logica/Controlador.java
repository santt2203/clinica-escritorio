package logica;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

    private static final String ALGORITMO_PASSWORD = "PBKDF2WithHmacSHA256";
    private static final String PREFIJO_HASH = "PBKDF2";
    private static final int ITERACIONES = 210_000;
    private static final int LONGITUD_SAL = 16;
    private static final int LONGITUD_HASH = 256;
    private static final SecureRandom GENERADOR_ALEATORIO = new SecureRandom();

    @Override
    public void registrarMedico(String email, String nombre, String password, String especialidad)
            throws UsuarioRepetidoException {
        verificarEmailLibre(email);
        ManejadorUsuario.getInstancia()
                .agregarUsuario(new Medico(email, nombre, generarHashPassword(password), especialidad));
    }

    @Override
    public void registrarPaciente(String email, String nombre, String password, String mutualista)
            throws UsuarioRepetidoException {
        verificarEmailLibre(email);
        ManejadorUsuario.getInstancia()
                .agregarUsuario(new Paciente(email, nombre, generarHashPassword(password), mutualista));
    }

    @Override
    public DtUsuario iniciarSesion(String email, String password) throws CredencialesInvalidasException {
        Usuario usuario = ManejadorUsuario.getInstancia().buscarUsuario(email);
        if (usuario == null || !verificarPassword(password, usuario.getPassword()))
            throw new CredencialesInvalidasException("Email o contraseña incorrectos");
        return usuario.getDtUsuario();
    }

    private void verificarEmailLibre(String email) throws UsuarioRepetidoException {
        if (ManejadorUsuario.getInstancia().buscarUsuario(email) != null)
            throw new UsuarioRepetidoException("Ya existe un usuario con el email " + email);
    }

    /** Genera un hash PBKDF2 con una sal aleatoria que se guarda junto al hash. */
    private static String generarHashPassword(String password) {
        try {
            byte[] sal = new byte[LONGITUD_SAL];
            GENERADOR_ALEATORIO.nextBytes(sal);
            byte[] hash = derivarHash(password, sal, ITERACIONES);
            return String.join("$", PREFIJO_HASH, Integer.toString(ITERACIONES),
                    Base64.getEncoder().encodeToString(sal), Base64.getEncoder().encodeToString(hash));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo proteger la contraseña", e);
        }
    }

    private static boolean verificarPassword(String password, String passwordGuardada) {
        if (passwordGuardada == null)
            return false;

        try {
            String[] partes = passwordGuardada.split("\\$", -1);
            if (partes.length != 4)
                return false;

            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[3]);
            byte[] hashObtenido = derivarHash(password, sal, iteraciones);
            return MessageDigest.isEqual(hashEsperado, hashObtenido);
        } catch (IllegalArgumentException | GeneralSecurityException e) {
            return false;
        }
    }

    private static byte[] derivarHash(String password, byte[] sal, int iteraciones)
            throws GeneralSecurityException {
        PBEKeySpec especificacion = new PBEKeySpec(password.toCharArray(), sal, iteraciones, LONGITUD_HASH);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO_PASSWORD)
                    .generateSecret(especificacion).getEncoded();
        } finally {
            especificacion.clearPassword();
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
