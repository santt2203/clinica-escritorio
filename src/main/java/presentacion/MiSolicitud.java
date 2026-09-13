package presentacion;

import java.awt.Component;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import datatypes.DtPrestacion;
import excepciones.OrdenVaciaException;
import interfaces.IControlador;

/** Solicitud de una orden médica: el paciente elige prestaciones, cantidades y confirma. */
public class MiSolicitud extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final String email;
    private final Map<Long, Integer> cantidades = new LinkedHashMap<>();

    private final DefaultTableModel modelo = modeloTabla(
            "Prestación", "Cantidad", "Precio unitario", "Subtotal");
    private final JTable tabla = new JTable(modelo);
    private final JComboBox<DtPrestacion> campoPrestacion = new JComboBox<>();
    private final JSpinner campoCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JButton botonAgregar;
    private final JLabel etiquetaTotal = new JLabel();

    public MiSolicitud(IControlador icon, String email) {
        super(icon, "Mi solicitud", 640, 460);
        this.email = email;

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

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);

        etiquetaTotal.setFont(Tema.CUERPO.deriveFont(Font.BOLD));

        agregarCampo("Prestación", campoPrestacion);
        agregarCampo("Cantidad", campoCantidad);
        botonAgregar = agregarBoton("Agregar", this::agregar);
        agregarCentro(new JScrollPane(tabla));
        agregarCampo("Total", etiquetaTotal);
        agregarBoton("Quitar seleccionada", this::quitarSeleccionada);
        agregarBoton("Confirmar solicitud", this::confirmar);
        agregarBoton("Cerrar", this::cerrar);
    }

    @Override
    protected void refrescar() {
        Long idSeleccionado = obtenerIdSeleccionado();
        campoPrestacion.removeAllItems();
        DtPrestacion seleccionada = null;
        for (DtPrestacion prestacion : icon.listarCatalogo()) {
            campoPrestacion.addItem(prestacion);
            if (prestacion.id().equals(idSeleccionado))
                seleccionada = prestacion;
        }
        campoPrestacion.setSelectedItem(seleccionada);
        botonAgregar.setEnabled(campoPrestacion.getItemCount() > 0);
        reconstruir();
    }

    private Long obtenerIdSeleccionado() {
        DtPrestacion seleccionada = (DtPrestacion) campoPrestacion.getSelectedItem();
        return seleccionada == null ? null : seleccionada.id();
    }

    private void agregar() {
        DtPrestacion prestacion = (DtPrestacion) campoPrestacion.getSelectedItem();
        if (prestacion == null)
            return;
        int cantidad = (Integer) campoCantidad.getValue();
        cantidades.merge(prestacion.id(), cantidad, Integer::sum);
        reconstruir();
    }

    private void quitarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarMensaje("Seleccioná una línea de la solicitud.", "Quitar prestación", true);
            return;
        }
        Long id = (Long) modelo.getValueAt(fila, 0);
        cantidades.remove(id);
        reconstruir();
    }

    private void reconstruir() {
        List<DtPrestacion> catalogo = icon.listarCatalogo();
        modelo.setRowCount(0);
        double total = 0;
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            double precio = catalogo.stream()
                    .filter(prestacion -> prestacion.id().equals(entrada.getKey()))
                    .mapToDouble(DtPrestacion::precio)
                    .findFirst()
                    .orElse(0);
            double subtotal = precio * entrada.getValue();
            total += subtotal;
            modelo.addRow(new Object[] {
                    entrada.getKey(), nombreDe(catalogo, entrada.getKey()),
                    entrada.getValue(), String.format("$ %.2f", precio),
                    String.format("$ %.2f", subtotal)
            });
        }
        etiquetaTotal.setText(String.format("$ %.2f", total));
    }

    private String nombreDe(List<DtPrestacion> catalogo, Long id) {
        return catalogo.stream()
                .filter(prestacion -> prestacion.id().equals(id))
                .map(DtPrestacion::nombre)
                .findFirst()
                .orElse("?");
    }

    private void confirmar() {
        if (cantidades.isEmpty()) {
            mostrarMensaje("La solicitud está vacía, agregá al menos una prestación.",
                    "Solicitud vacía", true);
            return;
        }

        double total = totalVigente();

        int respuesta = JOptionPane.showConfirmDialog(this, resumenSolicitud(total),
                "Confirmar solicitud", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION)
            return;

        try {
            icon.confirmarOrden(email, cantidades);
            mostrarMensaje("Solicitud confirmada por " + String.format("$ %.2f", total) + ".",
                    "Solicitud confirmada", false);
            cantidades.clear();
            reconstruir();
        } catch (OrdenVaciaException | IllegalArgumentException e) {
            mostrarMensaje(e.getMessage(), "No se pudo confirmar", true);
        }
    }

    private double totalVigente() {
        double total = 0;
        for (Long id : cantidades.keySet()) {
            DtPrestacion prestacion = icon.obtenerPrestacion(id);
            total += prestacion.precio() * cantidades.get(id);
        }
        return total;
    }

    private String resumenSolicitud(double total) {
        StringBuilder texto = new StringBuilder("¿Confirmar la solicitud?\n\n");
        List<DtPrestacion> catalogo = icon.listarCatalogo();
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            double precio = catalogo.stream()
                    .filter(prestacion -> prestacion.id().equals(entrada.getKey()))
                    .mapToDouble(DtPrestacion::precio)
                    .findFirst()
                    .orElse(0);
            texto.append(nombreDe(catalogo, entrada.getKey())).append(" ×")
                    .append(entrada.getValue())
                    .append("  ·  $ ").append(String.format("%.2f", precio * entrada.getValue()))
                    .append("\n");
        }
        texto.append("\nTotal: $ ").append(String.format("%.2f", total));
        return texto.toString();
    }

    private void mostrarMensaje(String mensaje, String titulo, boolean error) {
        JOptionPane.showMessageDialog(this, mensaje, titulo,
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}