package presentacion;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowSorter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import datatypes.DtEstudio;
import datatypes.DtPrestacion;
import datatypes.DtTerapia;
import interfaces.IControlador;

/** Ventana de consulta del catálogo de prestaciones. */
public class Catalogo extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final String email;
    private final DefaultTableModel modelo = modeloTabla(
            "ID", "Nombre", "Tipo", "Precio", "Franja", "Detalle");
    private final JTable tabla = new JTable(modelo);
    private final JTextField campoBusqueda = new JTextField();

    public Catalogo(IControlador icon) {
        this(icon, null);
    }

    public Catalogo(IControlador icon, String email) {
        super(icon, "Catálogo de prestaciones", 820, 420);
        this.email = email;

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);
        configurarOrdenamiento();

        agregarCampo("Buscar", campoBusqueda);
        campoBusqueda.addActionListener(e -> refrescar());
        agregarCentro(new JScrollPane(tabla));

        agregarBoton("Buscar", this::refrescar);
        agregarBoton("Ver todas", this::verTodas);
        agregarBoton("Ver detalle", this::verDetalle);
        agregarBoton("Actualizar", this::refrescar);
        if (email != null && !email.isBlank()) {
            agregarBoton("Añadir a seguidas", this::agregarASeguidas);
        }
        agregarBoton("Cerrar", this::cerrar);
    }

    private void configurarOrdenamiento() {
        TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modelo);
        ordenador.setMaxSortKeys(1);
        ordenador.setSortable(0, false);
        ordenador.setSortable(2, false);
        ordenador.setSortable(4, false);
        ordenador.setSortable(5, false);
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

    private void verTodas() {
        campoBusqueda.setText("");
        refrescar();
    }

    private void verDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una prestación del catálogo.",
                    "Ver detalle", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            int filaModelo = tabla.convertRowIndexToModel(fila);
            Long id = ((Number) modelo.getValueAt(filaModelo, 0)).longValue();
            mostrarDetalle(icon.obtenerPrestacion(id));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo consultar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalle(DtPrestacion prestacion) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 8));
        panel.setBackground(Tema.TARJETA);
        agregarValor(panel, "Nombre", prestacion.nombre());
        agregarValor(panel, "Precio", String.format("$ %.2f", prestacion.precio()));
        agregarValor(panel, "Franja", prestacion.franja().toString());
        switch (prestacion) {
            case DtEstudio estudio -> {
                agregarValor(panel, "Duración", estudio.duracionMinutos() + " minutos");
            }
            case DtTerapia terapia -> {
                agregarValor(panel, "Requiere derivación", terapia.requiereDerivacion() ? "Sí" : "No");
                agregarValor(panel, "Cantidad de sesiones", terapia.cantidadSesiones() + " sesiones");
            }
        }
        JOptionPane.showMessageDialog(this, panel, "Detalle de " + prestacion.nombre(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarValor(JPanel panel, String etiqueta, String valor) {
        JTextField campo = new JTextField(valor);
        campo.setEditable(false);
        panel.add(Tema.etiqueta(etiqueta));
        panel.add(campo);
    }

       @Override
    protected void refrescar() {
        modelo.setRowCount(0);
        String texto = campoBusqueda.getText().trim();
        List<DtPrestacion> prestaciones = texto.isEmpty()
                ? icon.listarCatalogo()
                : icon.buscarPrestaciones(texto);
        for (DtPrestacion prestacion : prestaciones) {
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
            }
            modelo.addRow(new Object[] {
                    prestacion.id(), prestacion.nombre(), tipo,
                    prestacion.precio(),
                    prestacion.franja(), detalle
            });
        }
    }

    private void agregarASeguidas() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una prestación para agregarla a seguidas");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Long idPrestacion = ((Number) modelo.getValueAt(filaModelo, 0)).longValue();
        try {
            icon.agregarSeguido(email, idPrestacion);
            JOptionPane.showMessageDialog(this, "Prestación agregada a seguidas");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
