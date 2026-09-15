package presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import interfaces.IControlador;

/**
 * Clase base de todas las ventanas del sistema: concentra la configuracion que
 * de otra forma habria que repetir en cada una.
 */
public abstract class VentanaInterna extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected final IControlador icon;

    /** Formulario en dos columnas: etiqueta a la izquierda, campo a la derecha. */
    private final JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));

    /** Franja inferior donde van los botones. */
    private final JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    protected VentanaInterna(IControlador icon, String titulo, int ancho, int alto) {
        this.icon = icon;
        setTitle(titulo);
        setSize(ancho, alto);
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        formulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 12, 16));
        formulario.setBackground(Tema.TARJETA);
        botones.setBackground(Tema.TARJETA);
        getContentPane().setBackground(Tema.TARJETA);
        add(formulario, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Se ejecuta cada vez que la ventana se abre. Las ventanas que muestran datos
     * la sobreescriben para volver a leerlos de la base.
     */
    protected void refrescar() {
    }

    /** Agrega una fila "etiqueta + campo" al formulario. */
    protected void agregarCampo(String etiqueta, java.awt.Component campo) {
        formulario.add(new JLabel(etiqueta));
        formulario.add(campo);
    }

    /** Agrega un boton a la franja inferior y le engancha lo que tiene que hacer. */
    protected JButton agregarBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        botones.add(boton);
        return boton;
    }

    /** Lo que va en el centro de la ventana (una tabla, normalmente). */
    protected void agregarCentro(java.awt.Component componente) {
        add(componente, BorderLayout.CENTER);
    }

    /** Tabla de solo lectura: el usuario mira, no edita las celdas. */
    protected static DefaultTableModel modeloTabla(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    protected void cerrar() {
        setVisible(false);
    }
}
