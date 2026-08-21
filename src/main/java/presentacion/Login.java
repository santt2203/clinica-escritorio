package presentacion;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import datatypes.DtUsuario;
import excepciones.CredencialesInvalidasException;
import interfaces.Fabrica;
import interfaces.IControlador;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private final IControlador icon;
    private final JTextField campoEmail = new JTextField();
    private final JPasswordField campoPassword = new JPasswordField();

    public static void main(String[] args) {
        Tema.aplicar();
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

    public Login() {
        this.icon = Fabrica.getInstancia().getIControlador();

        setTitle("Clínica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(880, 540);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        getContentPane().setBackground(Tema.FONDO);

        JLabel heading = new JLabel("Ingresar");
        heading.setFont(Tema.TITULO.deriveFont(26f));
        heading.setForeground(Tema.TEXTO);

        JLabel ayuda = new JLabel("Usá tu email y contraseña para entrar.");
        ayuda.setFont(Tema.SUBTITULO);
        ayuda.setForeground(Tema.TEXTO_SUAVE);

        JPanel encabezado = new JPanel(new GridBagLayout());
        encabezado.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        encabezado.add(heading, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 0, 0);
        encabezado.add(ayuda, gbc);

        JButton botonIngresar = Tema.primario("Ingresar");
        botonIngresar.addActionListener(e -> ingresar());

        JButton botonRegistrarse = Tema.secundario("Crear cuenta");
        botonRegistrarse.addActionListener(e -> new Registro(this, icon).setVisible(true));

        JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
        botones.setOpaque(false);
        botones.add(botonIngresar);
        botones.add(botonRegistrarse);

        JPanel tarjeta = new JPanel(new BorderLayout(0, 28));
        tarjeta.setBackground(Tema.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(Tema.formulario(
                Tema.campo("Email", campoEmail),
                Tema.campo("Contraseña", campoPassword)), BorderLayout.CENTER);
        tarjeta.add(botones, BorderLayout.SOUTH);

        JPanel derecha = new JPanel(new GridBagLayout());
        derecha.setBackground(Tema.FONDO);
        derecha.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));
        derecha.add(tarjeta);

        add(Tema.marca(), BorderLayout.WEST);
        add(derecha, BorderLayout.CENTER);

        getRootPane().setDefaultButton(botonIngresar);
    }

    private void ingresar() {
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completá email y contraseña", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            DtUsuario usuario = icon.iniciarSesion(email, password);
            new Principal(icon, usuario).setVisible(true);
            dispose();
        } catch (CredencialesInvalidasException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
