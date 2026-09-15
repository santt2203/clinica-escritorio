package presentacion;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import datatypes.DtEstudio;
import datatypes.DtPrestacion;
import datatypes.DtTerapia;
import interfaces.IControlador;

/** Ventana de consulta del catálogo de prestaciones. */
public class Catalogo extends VentanaInterna {

    private static final long serialVersionUID = 1L;

    private final DefaultTableModel modelo = modeloTabla(
            "ID", "Nombre", "Tipo", "Precio", "Franja", "Detalle");
    private final JTextField campoBusqueda = new JTextField();
    private final JTable tabla = new JTable(modelo);

    public Catalogo(IControlador icon) {
        super(icon, "Catálogo de prestaciones", 820, 420);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);

        agregarCampo("Buscar", campoBusqueda);
        campoBusqueda.addActionListener(e -> refrescar());
        agregarCentro(new JScrollPane(tabla));
        agregarBoton("Buscar", this::refrescar);
        agregarBoton("Ver todas", this::verTodas);
        agregarBoton("Ver detalle", this::verDetalle);
        agregarBoton("Actualizar", this::refrescar);
        agregarBoton("Cerrar", this::cerrar);
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
            Long id = (Long) modelo.getValueAt(fila, 0);
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
                    String.format("$ %.2f", prestacion.precio()),
                    prestacion.franja(), detalle
            });
        }
    }
}
