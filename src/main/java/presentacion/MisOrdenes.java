package presentacion;

import java.awt.Component;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import datatypes.DtLineaOrden;
import datatypes.DtOrden;
import interfaces.IControlador;

/** Historial de órdenes médicas del paciente: las más recientes primero. */
public class MisOrdenes extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String email;
    private final JComboBox<DtOrden> campoOrden = new JComboBox<>();
    private final DefaultTableModel modelo = modeloTabla(
            "Prestación", "Cantidad", "Precio unitario", "Subtotal");
    private final JTable tabla = new JTable(modelo);
    private final JLabel etiquetaTotal = new JLabel();

    public MisOrdenes(IControlador icon, String email) {
        super(icon, "Mis órdenes", 660, 460);
        this.email = email;

        campoOrden.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean conFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, conFoco);
                if (valor instanceof DtOrden orden)
                    setText(FORMATO_FECHA.format(orden.fecha()) + "  ·  $ "
                            + String.format("%.2f", orden.total()));
                return this;
            }
        });

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);

        etiquetaTotal.setFont(Tema.CUERPO.deriveFont(Font.BOLD));

        campoOrden.addActionListener(evento -> cargarOrdenSeleccionada());
        agregarCampo("Órdenes (más reciente primero)", campoOrden);
        agregarCentro(new JScrollPane(tabla));
        agregarCampo("Total", etiquetaTotal);
        agregarBoton("Actualizar", this::refrescar);
        agregarBoton("Cerrar", this::cerrar);
    }

    @Override
    protected void refrescar() {
        Long idSeleccionado = obtenerIdSeleccionado();
        campoOrden.removeAllItems();
        DtOrden seleccionada = null;
        for (DtOrden orden : icon.listarOrdenes(email)) {
            campoOrden.addItem(orden);
            if (orden.id().equals(idSeleccionado))
                seleccionada = orden;
        }
        campoOrden.setSelectedItem(seleccionada);
        cargarOrdenSeleccionada();
    }

    private Long obtenerIdSeleccionado() {
        DtOrden seleccionada = (DtOrden) campoOrden.getSelectedItem();
        return seleccionada == null ? null : seleccionada.id();
    }

    private void cargarOrdenSeleccionada() {
        DtOrden orden = (DtOrden) campoOrden.getSelectedItem();
        modelo.setRowCount(0);
        if (orden == null) {
            etiquetaTotal.setText("Sin órdenes");
            return;
        }
        for (DtLineaOrden linea : orden.lineas()) {
            modelo.addRow(new Object[] {
                    linea.nombrePrestacion(), linea.cantidad(),
                    String.format("$ %.2f", linea.precioUnitario()),
                    String.format("$ %.2f", linea.subtotal())
            });
        }
        etiquetaTotal.setText(String.format("$ %.2f", orden.total()));
    }
}