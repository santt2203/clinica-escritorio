package interfaces;

import java.util.List;
import java.util.Map;

import datatypes.DtOrden;
import datatypes.DtPrestacion;
import datatypes.DtSeguido;
import datatypes.DtUsuario;
import excepciones.AccesoDenegadoException;
import excepciones.CredencialesInvalidasException;
import excepciones.OrdenVaciaException;
import excepciones.PrestacionEnOrdenException;
import excepciones.PrestacionRepetidaException;
import excepciones.SeguidoNoExisteException;
import excepciones.SeguidoRepetidoException;
import excepciones.UsuarioRepetidoException;

/**
 * Unico punto de contacto entre la presentacion y la logica.
 * La presentacion NO conoce las entidades, solo esta interfaz y los datatypes.
 */
public interface IControlador {

    void registrarMedico(String email, String nombre, String password, String especialidad)
            throws UsuarioRepetidoException;

    void registrarPaciente(String email, String nombre, String password, String mutualista)
            throws UsuarioRepetidoException;

    DtUsuario iniciarSesion(String email, String password) throws CredencialesInvalidasException;

    void agregarPrestacion(DtPrestacion prestacion)
            throws PrestacionRepetidaException, AccesoDenegadoException;

    void modificarPrestacion(DtPrestacion prestacion) throws PrestacionRepetidaException;

    void eliminarPrestacion(Long idPrestacion) throws PrestacionEnOrdenException;

    List<DtPrestacion> listarCatalogo();

    List<DtPrestacion> buscarPrestaciones(String texto);

    DtPrestacion obtenerPrestacion(Long idPrestacion);

    void agregarSeguido(String email, Long idPrestacion) throws SeguidoRepetidoException;

    void quitarSeguido(String email, Long idPrestacion) throws SeguidoNoExisteException;

    List<DtSeguido> listarSeguidos(String email);

    void confirmarOrden(String email, Map<Long, Integer> cantidadPorPrestacion) throws OrdenVaciaException;

    List<DtOrden> listarOrdenes(String email);
}
