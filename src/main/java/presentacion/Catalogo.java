package presentacion;

import java.awt.Font;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
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

    public Catalogo(IControlador icon) {
        super(icon, "Catálogo de prestaciones", 820, 420);

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);
        agregarCentro(new JScrollPane(tabla));
        agregarBoton("Actualizar", this::refrescar);
        agregarBoton("Cerrar", this::cerrar);
    }

    @Override
    protected void refrescar() {
        modelo.setRowCount(0);
        List<DtPrestacion> prestaciones = icon.listarCatalogo();
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
