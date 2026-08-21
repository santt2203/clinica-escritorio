package presentacion;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import excepciones.UsuarioRepetidoException;
import interfaces.IControlador;

public class Registro extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String MEDICO = "Médico";

    private final IControlador icon;
    private final JTextField campoEmail = new JTextField();
    private final JTextField campoNombre = new JTextField();
    private final JPasswordField campoPassword = new JPasswordField();
    private final JComboBox<String> campoTipo = new JComboBox<>(new String[] { "Paciente", MEDICO });
    private final JTextField campoExtra = new JTextField();
    private final JPanel grupoExtra = Tema.campo("Mutualista", campoExtra);

    public Registro(JFrame padre, IControlador icon) {
        super(padre, "Crear cuenta", true);
        this.icon = icon;

        campoTipo.addActionListener(e -> actualizarCampoExtra());

        JLabel heading = new JLabel("Crear cuenta");
        heading.setFont(Tema.TITULO.deriveFont(22f));
        heading.setForeground(Tema.TEXTO);

        JButton botonAceptar = Tema.primario("Registrarme");
        botonAceptar.addActionListener(e -> registrar());
        JButton botonCancelar = Tema.secundario("Cancelar");
        botonCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
        botones.setOpaque(false);
        botones.add(botonAceptar);
        botones.add(botonCancelar);

        JPanel contenido = new JPanel(new BorderLayout(0, 24));
        contenido.setBackground(Tema.TARJETA);
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        contenido.add(heading, BorderLayout.NORTH);
        contenido.add(Tema.formulario(
                Tema.campo("Email", campoEmail),
                Tema.campo("Nombre", campoNombre),
                Tema.campo("Contraseña", campoPassword),
                Tema.campo("Tipo de usuario", campoTipo),
                grupoExtra), BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);
        setSize(460, 620);
        setMinimumSize(getSize());
        setLocationRelativeTo(padre);
        getRootPane().setDefaultButton(botonAceptar);
    }

    private void actualizarCampoExtra() {
        JLabel etiqueta = (JLabel) grupoExtra.getComponent(0);
        etiqueta.setText(esMedico() ? "ESPECIALIDAD" : "MUTUALISTA");
    }

    private boolean esMedico() {
        return MEDICO.equals(campoTipo.getSelectedItem());
    }

    private void registrar() {
        String email = campoEmail.getText().trim();
        String nombre = campoNombre.getText().trim();
        String password = new String(campoPassword.getPassword());
        String extra = campoExtra.getText().trim();

        if (email.isEmpty() || nombre.isEmpty() || password.isEmpty() || extra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No puede haber campos vacíos", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "El email no es válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (esMedico())
                icon.registrarMedico(email, nombre, password, extra);
            else
                icon.registrarPaciente(email, nombre, password, extra);

            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito, ya podés ingresar");
            dispose();
        } catch (UsuarioRepetidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
