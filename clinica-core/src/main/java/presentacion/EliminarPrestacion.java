package presentacion;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import datatypes.DtPrestacion;
import excepciones.AccesoDenegadoException;
import excepciones.PrestacionEnOrdenException;
import interfaces.IControlador;

public class EliminarPrestacion extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final JComboBox<DtPrestacion> campoPrestacion = new JComboBox<>();

    public EliminarPrestacion(IControlador icon) {
        super(icon, "Eliminar prestación", 480, 180);

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

        agregarCampo("Prestación", campoPrestacion);
        agregarBoton("Cancelar", this::cerrar);
        agregarBoton("Eliminar", this::eliminar);
    }

    @Override
    protected void refrescar() {
        campoPrestacion.removeAllItems();
        for (DtPrestacion prestacion : icon.listarCatalogo())
            campoPrestacion.addItem(prestacion);
    }

    private void eliminar() {
        DtPrestacion prestacion = (DtPrestacion) campoPrestacion.getSelectedItem();
        if (prestacion == null) {
            mostrarMensaje("No hay prestaciones para eliminar.", "Catálogo vacío", true);
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la prestación \"" + prestacion.nombre() + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION)
            return;
        try {
            icon.eliminarPrestacion(prestacion.id());
            mostrarMensaje("La prestación se eliminó del catálogo.", "Prestación eliminada", false);
            refrescar();
        } catch (PrestacionEnOrdenException | AccesoDenegadoException e) {
            mostrarMensaje(e.getMessage(), "No se pudo eliminar", true);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage(), "Datos inválidos", true);
        }
    }

    private void mostrarMensaje(String mensaje, String titulo, boolean error) {
        JOptionPane.showMessageDialog(this, mensaje, titulo,
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}
