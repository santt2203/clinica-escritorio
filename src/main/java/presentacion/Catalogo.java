package presentacion;

import java.awt.Font;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
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

    private final String email;
    private final DefaultTableModel modelo = modeloTabla(
            "ID", "Nombre", "Tipo", "Precio", "Franja", "Detalle");
    private final JTable tabla = new JTable(modelo);
    private List<DtPrestacion> prestacionesActual = List.of();

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
        agregarCentro(new JScrollPane(tabla));

        agregarBoton("Precio ↑", () -> mostrarPrestaciones(ordenarPorPrecio(prestacionesActual, true)));
        agregarBoton("Precio ↓", () -> mostrarPrestaciones(ordenarPorPrecio(prestacionesActual, false)));
        agregarBoton("A-Z", () -> mostrarPrestaciones(ordenarAlfabeticamente(prestacionesActual, true)));
        agregarBoton("Z-A", () -> mostrarPrestaciones(ordenarAlfabeticamente(prestacionesActual, false)));
        agregarBoton("Actualizar", this::refrescar);
        if (email != null && !email.isBlank()) {
            agregarBoton("Añadir a seguidas", this::agregarASeguidas);
        }
        agregarBoton("Cerrar", this::cerrar);
    }

    @Override
    protected void refrescar() {
        prestacionesActual = icon.listarCatalogo();
        mostrarPrestaciones(prestacionesActual);
    }

    private void mostrarPrestaciones(List<DtPrestacion> prestaciones) {
        modelo.setRowCount(0);
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
                default -> {
                    tipo = "Prestación";
                    detalle = "";
                }
            }
            modelo.addRow(new Object[] {
                    prestacion.id(), prestacion.nombre(), tipo,
                    String.format("$ %.2f", prestacion.precio()),
                    prestacion.franja(), detalle
            });
        }
    }

    private List<DtPrestacion> ordenarPorPrecio(List<DtPrestacion> prestaciones, boolean ascendente) {
        Comparator<DtPrestacion> comparador = Comparator.comparingDouble(DtPrestacion::precio);
        return ascendente
                ? prestaciones.stream().sorted(comparador).toList()
                : prestaciones.stream().sorted(comparador.reversed()).toList();
    }

    private List<DtPrestacion> ordenarAlfabeticamente(List<DtPrestacion> prestaciones, boolean ascendente) {
        Comparator<DtPrestacion> comparador = Comparator.comparing(DtPrestacion::nombre,
                String.CASE_INSENSITIVE_ORDER);
        return ascendente
                ? prestaciones.stream().sorted(comparador).toList()
                : prestaciones.stream().sorted(comparador.reversed()).toList();
    }

    private void agregarASeguidas() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una prestación para agregarla a seguidas");
            return;
        }

        Long idPrestacion = ((Number) modelo.getValueAt(fila, 0)).longValue();
        try {
            icon.agregarSeguido(email, idPrestacion);
            JOptionPane.showMessageDialog(this, "Prestación agregada a seguidas");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
