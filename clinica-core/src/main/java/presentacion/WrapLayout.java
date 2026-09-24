package presentacion;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * FlowLayout que informa la altura real que necesitan sus filas.
 *
 * FlowLayout ya acomoda componentes en varias filas, pero calcula su tamaño
 * preferido como si todos entraran en una sola. En una zona BorderLayout.SOUTH
 * eso hace que las filas adicionales queden recortadas.
 */
final class WrapLayout extends FlowLayout {

    private static final long serialVersionUID = 1L;

    WrapLayout(int alineacion) {
        super(alineacion);
    }

    @Override
    public Dimension preferredLayoutSize(Container contenedor) {
        return calcularTamanio(contenedor, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container contenedor) {
        return calcularTamanio(contenedor, false);
    }

    private Dimension calcularTamanio(Container contenedor, boolean preferido) {
        synchronized (contenedor.getTreeLock()) {
            int anchoDisponible = obtenerAnchoDisponible(contenedor);
            if (anchoDisponible <= 0) {
                return preferido
                        ? super.preferredLayoutSize(contenedor)
                        : super.minimumLayoutSize(contenedor);
            }

            Insets margenes = contenedor.getInsets();
            int anchoInterior = Math.max(1,
                    anchoDisponible - margenes.left - margenes.right - getHgap() * 2);

            int anchoMaximo = 0;
            int anchoFila = 0;
            int altoFila = 0;
            int altoFilas = 0;

            for (Component componente : contenedor.getComponents()) {
                if (!componente.isVisible())
                    continue;

                Dimension tamanio = preferido
                        ? componente.getPreferredSize()
                        : componente.getMinimumSize();
                int separacion = anchoFila == 0 ? 0 : getHgap();

                if (anchoFila > 0 && anchoFila + separacion + tamanio.width > anchoInterior) {
                    anchoMaximo = Math.max(anchoMaximo, anchoFila);
                    altoFilas += altoFila + (altoFilas == 0 ? 0 : getVgap());
                    anchoFila = 0;
                    altoFila = 0;
                    separacion = 0;
                }

                anchoFila += separacion + tamanio.width;
                altoFila = Math.max(altoFila, tamanio.height);
            }

            if (anchoFila > 0) {
                anchoMaximo = Math.max(anchoMaximo, anchoFila);
                altoFilas += altoFila + (altoFilas == 0 ? 0 : getVgap());
            }

            return new Dimension(
                    anchoMaximo + margenes.left + margenes.right + getHgap() * 2,
                    altoFilas + margenes.top + margenes.bottom + getVgap() * 2);
        }
    }

    private int obtenerAnchoDisponible(Container contenedor) {
        Container padre = contenedor.getParent();
        if (padre != null && padre.getWidth() > 0)
            return padre.getWidth();
        return contenedor.getWidth();
    }
}
