package presentacion;

import java.awt.Component;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import datatypes.DtEstudio;
import datatypes.DtPrestacion;
import datatypes.DtSeguido;
import datatypes.DtTerapia;
import interfaces.IControlador;

/** Ventana con las prestaciones marcadas como seguidas por el paciente. */
public class Seguidas extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final String email;
    private final DefaultTableModel modelo = modeloTabla(
            "ID", "Nombre", "Tipo", "Precio", "Franja", "Detalle", "Fecha");
    private final JTable tabla = new JTable(modelo);

    public Seguidas(IControlador icon, String email) {
        super(icon, "Prestaciones seguidas", 900, 420);
        this.email = email;

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);
        configurarOrdenamiento();

        agregarCentro(new JScrollPane(tabla));

        agregarBoton("Actualizar", this::refrescar);
        agregarBoton("Eliminar de seguidas", this::eliminarSeleccionada);
        agregarBoton("Cerrar", this::cerrar);
    }

    private void configurarOrdenamiento() {
        TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modelo);
        ordenador.setMaxSortKeys(1);
        ordenador.setSortable(0, false);
        ordenador.setSortable(2, false);
        ordenador.setSortable(4, false);
        ordenador.setSortable(5, false);
        ordenador.setSortable(6, false);
        ordenador.setComparator(1, String.CASE_INSENSITIVE_ORDER);
        ordenador.setComparator(3,
                (primero, segundo) -> Double.compare(
                        ((Number) primero).doubleValue(),
                        ((Number) segundo).doubleValue()));
        ordenador.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        tabla.setRowSorter(ordenador);

        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor,
                    boolean seleccionado, boolean enfocado, int fila, int columna) {
                JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(
                        tabla, valor, seleccionado, enfocado, fila, columna);
                etiqueta.setText(String.format("$ %.2f", ((Number) valor).doubleValue()));
                etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);
                return etiqueta;
            }
        });
    }

    @Override
    protected void refrescar() {
        mostrarSeguidos(icon.listarSeguidos(email));
    }

    private void mostrarSeguidos(List<DtSeguido> seguidos) {
        modelo.setRowCount(0);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (DtSeguido seguido : seguidos) {
            DtPrestacion prestacion = seguido.prestacion();
            String tipo;
            String detalle;
            switch (prestacion) {
                case DtEstudio estudio -> {
                    tipo = "Estudio";
                    detalle = estudio.duracionMinutos() + " minutos";
                }
                case DtTerapia terapia -> {
                    tipo = "Terapia";
                    detalle = terapia.cantidadSesiones() + " sesiones"
                            + (terapia.requiereDerivacion() ? " · requiere derivación" : "");
                }
                default -> {
                    tipo = "Prestación";
                    detalle = "";
                }
            }

            modelo.addRow(new Object[] {
                    prestacion.id(),
                    prestacion.nombre(),
                    tipo,
                    prestacion.precio(),
                    prestacion.franja(),
                    detalle,
                    seguido.fecha().format(formatoFecha)
            });
        }
    }

    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una prestación para eliminarla de seguidas");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Long idPrestacion = ((Number) modelo.getValueAt(filaModelo, 0)).longValue();
        try {
            icon.quitarSeguido(email, idPrestacion);
            refrescar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
