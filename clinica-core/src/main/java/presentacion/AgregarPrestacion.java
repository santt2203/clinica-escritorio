package presentacion;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import datatypes.DtEstudio;
import datatypes.DtPrestacion;
import datatypes.DtTerapia;
import datatypes.Franja;
import excepciones.AccesoDenegadoException;
import excepciones.PrestacionRepetidaException;
import interfaces.IControlador;

public class AgregarPrestacion extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private static final String ESTUDIO = "Estudio";
    private static final String TERAPIA = "Terapia";

    private final JComboBox<String> campoTipo = new JComboBox<>(new String[] { ESTUDIO, TERAPIA });
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoPrecio = new JTextField();
    private final JComboBox<Franja> campoFranja = new JComboBox<>(Franja.values());
    private final JSpinner campoDuracion = new JSpinner(new SpinnerNumberModel(30, 1, 1440, 1));
    private final JCheckBox campoRequiereDerivacion = new JCheckBox("Requiere derivación");
    private final JSpinner campoCantidadSesiones = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

    public AgregarPrestacion(IControlador icon) {
        super(icon, "Agregar prestación", 480, 400);

        agregarCampo("Tipo", campoTipo);
        agregarCampo("Nombre", campoNombre);
        agregarCampo("Precio", campoPrecio);
        agregarCampo("Franja", campoFranja);
        agregarCampo("Duración (minutos)", campoDuracion);
        agregarCampo("Derivación", campoRequiereDerivacion);
        agregarCampo("Cantidad de sesiones", campoCantidadSesiones);

        campoTipo.addActionListener(this::actualizarCamposSegunTipo);
        actualizarCamposSegunTipo(null);

        agregarBoton("Cancelar", this::cerrar);
        agregarBoton("Guardar", this::guardar);
    }

    private void actualizarCamposSegunTipo(ActionEvent evento) {
        boolean esEstudio = ESTUDIO.equals(campoTipo.getSelectedItem());
        campoDuracion.setEnabled(esEstudio);
        campoRequiereDerivacion.setEnabled(!esEstudio);
        campoCantidadSesiones.setEnabled(!esEstudio);
    }

    private void guardar() {
        try {
            DtPrestacion prestacion = construirPrestacion();
            icon.agregarPrestacion(prestacion);
            mostrarMensaje("La prestación se agregó al catálogo.", "Prestación agregada", false);
            cerrar();
        } catch (NumberFormatException e) {
            mostrarMensaje("El precio debe ser un número válido.", "Datos inválidos", true);
        } catch (PrestacionRepetidaException | AccesoDenegadoException e) {
            mostrarMensaje(e.getMessage(), "No se pudo agregar", true);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage(), "Datos inválidos", true);
        }
    }

    private DtPrestacion construirPrestacion() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!nombre.matches("[\\p{L}]+(?:[\\s'-]+[\\p{L}]+)*"))
            throw new IllegalArgumentException("El nombre solo puede contener letras.");

        double precio = Double.parseDouble(campoPrecio.getText().trim());
        Franja franja = (Franja) campoFranja.getSelectedItem();
        if (!Double.isFinite(precio) || precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");

        if (ESTUDIO.equals(campoTipo.getSelectedItem())) {
            return new DtEstudio(null, nombre, precio, franja, (Integer) campoDuracion.getValue());
        }
        return new DtTerapia(null, nombre, precio, franja,
                campoRequiereDerivacion.isSelected(), (Integer) campoCantidadSesiones.getValue());
    }

    private void mostrarMensaje(String mensaje, String titulo, boolean error) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo,
                error ? javax.swing.JOptionPane.ERROR_MESSAGE : javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
