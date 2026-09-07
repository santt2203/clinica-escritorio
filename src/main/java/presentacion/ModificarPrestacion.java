package presentacion;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
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

public class ModificarPrestacion extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final JComboBox<DtPrestacion> campoPrestacion = new JComboBox<>();
    private final JTextField campoTipo = new JTextField();
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoPrecio = new JTextField();
    private final JComboBox<Franja> campoFranja = new JComboBox<>(Franja.values());
    private final JSpinner campoDuracion = new JSpinner(new SpinnerNumberModel(30, 1, 1440, 1));
    private final JCheckBox campoRequiereDerivacion = new JCheckBox("Requiere derivación");
    private final JSpinner campoCantidadSesiones = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private final JButton botonGuardar;

    public ModificarPrestacion(IControlador icon) {
        super(icon, "Modificar prestación", 500, 430);

        campoPrestacion.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean conFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, conFoco);
                if (valor instanceof DtPrestacion prestacion)
                    setText(prestacion.nombre() + "  ·  $ " + String.format("%.2f", prestacion.precio()));
                return this;
            }
        });
        campoTipo.setEditable(false);

        agregarCampo("Prestación", campoPrestacion);
        agregarCampo("Tipo", campoTipo);
        agregarCampo("Nombre", campoNombre);
        agregarCampo("Precio", campoPrecio);
        agregarCampo("Franja", campoFranja);
        agregarCampo("Duración (minutos)", campoDuracion);
        agregarCampo("Derivación", campoRequiereDerivacion);
        agregarCampo("Cantidad de sesiones", campoCantidadSesiones);

        campoPrestacion.addActionListener(evento -> cargarPrestacionSeleccionada());

        agregarBoton("Cancelar", this::cerrar);
        botonGuardar = agregarBoton("Guardar", this::guardar);
    }

    @Override
    protected void refrescar() {
        Long idSeleccionado = obtenerIdSeleccionado();
        campoPrestacion.removeAllItems();

        DtPrestacion primera = null;
        DtPrestacion seleccionada = null;
        for (DtPrestacion prestacion : icon.listarCatalogo()) {
            if (primera == null)
                primera = prestacion;
            campoPrestacion.addItem(prestacion);
            if (prestacion.id().equals(idSeleccionado))
                seleccionada = prestacion;
        }

        campoPrestacion.setSelectedItem(seleccionada != null ? seleccionada : primera);
        cargarPrestacionSeleccionada();
    }

    private Long obtenerIdSeleccionado() {
        DtPrestacion seleccionada = (DtPrestacion) campoPrestacion.getSelectedItem();
        return seleccionada == null ? null : seleccionada.id();
    }

    private void cargarPrestacionSeleccionada() {
        DtPrestacion prestacion = (DtPrestacion) campoPrestacion.getSelectedItem();
        boolean haySeleccion = prestacion != null;
        campoNombre.setEnabled(haySeleccion);
        campoPrecio.setEnabled(haySeleccion);
        campoFranja.setEnabled(haySeleccion);
        botonGuardar.setEnabled(haySeleccion);

        if (!haySeleccion) {
            campoTipo.setText("");
            campoNombre.setText("");
            campoPrecio.setText("");
            campoDuracion.setEnabled(false);
            campoRequiereDerivacion.setEnabled(false);
            campoCantidadSesiones.setEnabled(false);
            return;
        }

        campoNombre.setText(prestacion.nombre());
        campoPrecio.setText(Double.toString(prestacion.precio()));
        campoFranja.setSelectedItem(prestacion.franja());

        switch (prestacion) {
            case DtEstudio estudio -> {
                campoTipo.setText("Estudio");
                campoDuracion.setValue(estudio.duracionMinutos());
                campoDuracion.setEnabled(true);
                campoRequiereDerivacion.setSelected(false);
                campoRequiereDerivacion.setEnabled(false);
                campoCantidadSesiones.setValue(1);
                campoCantidadSesiones.setEnabled(false);
            }
            case DtTerapia terapia -> {
                campoTipo.setText("Terapia");
                campoDuracion.setValue(30);
                campoDuracion.setEnabled(false);
                campoRequiereDerivacion.setSelected(terapia.requiereDerivacion());
                campoRequiereDerivacion.setEnabled(true);
                campoCantidadSesiones.setValue(terapia.cantidadSesiones());
                campoCantidadSesiones.setEnabled(true);
            }
        }
    }

    private void guardar() {
        try {
            DtPrestacion prestacion = construirPrestacion();
            icon.modificarPrestacion(prestacion);
            mostrarMensaje("La prestación se modificó.", "Prestación modificada", false);
            cerrar();
        } catch (NumberFormatException e) {
            mostrarMensaje("El precio debe ser un número válido.", "Datos inválidos", true);
        } catch (PrestacionRepetidaException | AccesoDenegadoException e) {
            mostrarMensaje(e.getMessage(), "No se pudo modificar", true);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage(), "Datos inválidos", true);
        }
    }

    private DtPrestacion construirPrestacion() {
        DtPrestacion seleccionada = (DtPrestacion) campoPrestacion.getSelectedItem();
        if (seleccionada == null)
            throw new IllegalArgumentException("No hay prestaciones para modificar.");

        String nombre = campoNombre.getText().trim();
        if (nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!nombre.matches("[\\p{L}]+(?:[\\s'-]+[\\p{L}]+)*"))
            throw new IllegalArgumentException("El nombre solo puede contener letras.");

        double precio = Double.parseDouble(campoPrecio.getText().trim());
        Franja franja = (Franja) campoFranja.getSelectedItem();
        if (!Double.isFinite(precio) || precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");

        return switch (seleccionada) {
            case DtEstudio estudio -> new DtEstudio(estudio.id(), nombre, precio, franja,
                    (Integer) campoDuracion.getValue());
            case DtTerapia terapia -> new DtTerapia(terapia.id(), nombre, precio, franja,
                    campoRequiereDerivacion.isSelected(), (Integer) campoCantidadSesiones.getValue());
        };
    }

    private void mostrarMensaje(String mensaje, String titulo, boolean error) {
        JOptionPane.showMessageDialog(this, mensaje, titulo,
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}
