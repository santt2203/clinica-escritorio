package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import datatypes.DtMedico;
import datatypes.DtPaciente;
import datatypes.DtUsuario;
import interfaces.IControlador;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JDesktopPane escritorio = new JDesktopPane();
    private final IControlador icon;

    public Principal(IControlador icon, DtUsuario usuario) {
        this.icon = icon;
        setTitle("Clínica");
        setSize(1100, 680);
        setMinimumSize(getSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String rol = switch (usuario) {
            case DtMedico medico -> "Médico · " + medico.especialidad();
            case DtPaciente paciente -> "Paciente · " + paciente.mutualista();
        };

        JLabel marca = new JLabel("Clínica");
        marca.setFont(Tema.TITULO.deriveFont(20f));
        marca.setForeground(Color.WHITE);

        JLabel sesion = new JLabel(usuario.nombre() + "  ·  " + rol);
        sesion.setFont(Tema.CUERPO);
        sesion.setForeground(new Color(210, 236, 236));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(Tema.MARCA);
        cabecera.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        cabecera.add(marca, BorderLayout.WEST);
        cabecera.add(sesion, BorderLayout.EAST);

        escritorio.setBackground(Tema.FONDO);
        escritorio.setBorder(null);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(cabecera, BorderLayout.NORTH);
        raiz.add(escritorio, BorderLayout.CENTER);
        setContentPane(raiz);

        JMenuBar barra = new JMenuBar();
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE));
        barra.setBackground(Tema.TARJETA);
        barra.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));
        setJMenuBar(barra);

        switch (usuario) {
            case DtMedico medico -> menuMedico(barra);
            case DtPaciente paciente -> menuPaciente(barra);
        }

        JMenu salir = new JMenu("Sesión");
        salir.add(item("Cerrar sesión", () -> {
            dispose();
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        }));
        barra.add(salir);
    }

    private void menuMedico(JMenuBar barra) {
        JMenu prestaciones = new JMenu("Prestaciones");
        prestaciones.add(item("Agregar prestación", () -> mostrar(new AgregarPrestacion(icon))));
        prestaciones.add(itemPendiente("Modificar prestación"));
        prestaciones.add(itemPendiente("Eliminar prestación"));
        barra.add(prestaciones);

        JMenu catalogo = new JMenu("Catálogo");
        catalogo.add(item("Ver el catálogo", () -> mostrar(new Catalogo(icon))));
        barra.add(catalogo);
    }

    private void menuPaciente(JMenuBar barra) {
        JMenu catalogo = new JMenu("Catálogo");
        catalogo.add(item("Ver el catálogo", () -> mostrar(new Catalogo(icon))));
        barra.add(catalogo);

        JMenu mio = new JMenu("Mi cuenta");
        mio.add(itemPendiente("Prestaciones seguidas"));
        mio.add(itemPendiente("Mi solicitud"));
        mio.add(itemPendiente("Mis órdenes"));
        barra.add(mio);
    }

    private JMenuItem item(String texto, Runnable accion) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> accion.run());
        return item;
    }

    private JMenuItem itemPendiente(String texto) {
        return item(texto, () -> JOptionPane.showMessageDialog(this, "Todavía no está implementado"));
    }

    protected void mostrar(VentanaInterna ventana) {
        if (ventana.getParent() == null) {
            escritorio.add(ventana);
            ventana.setLocation((escritorio.getWidth() - ventana.getWidth()) / 2,
                    (escritorio.getHeight() - ventana.getHeight()) / 2);
        }
        ventana.refrescar();
        ventana.setVisible(true);
        ventana.toFront();
    }
}
