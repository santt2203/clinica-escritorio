package presentacion;

import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

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
    private List<DtSeguido> seguidosActual = List.of();

    public Seguidas(IControlador icon, String email) {
        super(icon, "Prestaciones seguidas", 900, 420);
        this.email = email;

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(Tema.CUERPO.deriveFont(Font.BOLD));
        tabla.setFillsViewportHeight(true);

        agregarCentro(new JScrollPane(tabla));

        agregarBoton("Precio ↑", () -> mostrarSeguidos(ordenarPorPrecio(seguidosActual, true)));
        agregarBoton("Precio ↓", () -> mostrarSeguidos(ordenarPorPrecio(seguidosActual, false)));
        agregarBoton("A-Z", () -> mostrarSeguidos(ordenarAlfabeticamente(seguidosActual, true)));
        agregarBoton("Z-A", () -> mostrarSeguidos(ordenarAlfabeticamente(seguidosActual, false)));
        agregarBoton("Actualizar", this::refrescar);
        agregarBoton("Eliminar de seguidas", this::eliminarSeleccionada);
        agregarBoton("Cerrar", this::cerrar);
    }

    @Override
    protected void refrescar() {
        seguidosActual = icon.listarSeguidos(email);
        mostrarSeguidos(seguidosActual);
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
                    String.format("$ %.2f", prestacion.precio()),
                    prestacion.franja(),
                    detalle,
                    seguido.fecha().format(formatoFecha)
            });
        }
    }

    private List<DtSeguido> ordenarPorPrecio(List<DtSeguido> seguidos, boolean ascendente) {
        Comparator<DtSeguido> comparador = Comparator.comparingDouble(seguido -> seguido.prestacion().precio());
        return ascendente
                ? seguidos.stream().sorted(comparador).toList()
                : seguidos.stream().sorted(comparador.reversed()).toList();
    }

    private List<DtSeguido> ordenarAlfabeticamente(List<DtSeguido> seguidos, boolean ascendente) {
        Comparator<DtSeguido> comparador = Comparator.comparing(
                seguido -> seguido.prestacion().nombre(),
                String.CASE_INSENSITIVE_ORDER);
        return ascendente
                ? seguidos.stream().sorted(comparador).toList()
                : seguidos.stream().sorted(comparador.reversed()).toList();
    }

    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una prestación para eliminarla de seguidas");
            return;
        }

        Long idPrestacion = ((Number) modelo.getValueAt(fila, 0)).longValue();
        try {
            icon.quitarSeguido(email, idPrestacion);
            refrescar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
