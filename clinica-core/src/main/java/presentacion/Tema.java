package presentacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Paleta y look and feel de las ventanas. Se aplica una sola vez al arrancar.
 */
public final class Tema {

    static final Color MARCA = new Color(13, 107, 108);
    static final Color MARCA_OSCURA = new Color(8, 78, 80);
    static final Color FONDO = new Color(244, 249, 249);
    static final Color TARJETA = Color.WHITE;
    static final Color TEXTO = new Color(28, 40, 44);
    static final Color TEXTO_SUAVE = new Color(90, 110, 112);
    static final Color BORDE = new Color(214, 228, 228);

    static final Font TITULO = new Font("SansSerif", Font.BOLD, 30);
    static final Font SUBTITULO = new Font("SansSerif", Font.PLAIN, 15);
    static final Font ETIQUETA = new Font("SansSerif", Font.BOLD, 12);
    static final Font CUERPO = new Font("SansSerif", Font.PLAIN, 14);

    private Tema() {
    }

    static void aplicar() {
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 14);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("TextComponent.padding", new Insets(8, 12, 8, 12));
        UIManager.put("Component.focusColor", MARCA);
        UIManager.put("Component.accentColor", MARCA);
        UIManager.put("Button.default.background", MARCA);
        UIManager.put("Button.default.foreground", Color.WHITE);
        UIManager.put("Button.default.hoverBackground", MARCA_OSCURA);
        UIManager.put("Button.hoverBackground", MARCA_OSCURA);
        UIManager.put("Button.hoverForeground", TEXTO);
        UIManager.put("Button.default.hoverForeground", Color.WHITE);
        UIManager.put("TitlePane.unifiedBackground", false);
        UIManager.put("defaultFont", CUERPO);
    }

    static JLabel etiqueta(String texto) {
        JLabel label = new JLabel(texto.toUpperCase());
        label.setFont(ETIQUETA);
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    static JButton primario(String texto) {
        JButton boton = new JButton(texto);
        boton.putClientProperty("JButton.buttonType", "default");
        boton.setBackground(MARCA);
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(0, 40));
        return boton;
    }

    static JButton secundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(0, 40));
        return boton;
    }

    static JPanel formulario(Component... filas) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        int fila = 0;
        for (Component componente : filas) {
            gbc.gridy = fila++;
            gbc.insets = new Insets(fila == 1 ? 0 : 14, 0, 0, 0);
            panel.add(componente, gbc);
        }
        return panel;
    }

    static JPanel campo(String textoEtiqueta, JComponent entrada) {
        entrada.setFont(CUERPO);
        entrada.setPreferredSize(new Dimension(280, 40));
        JPanel grupo = new JPanel(new GridBagLayout());
        grupo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        grupo.add(etiqueta(textoEtiqueta), gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(6, 0, 0, 0);
        grupo.add(entrada, gbc);
        return grupo;
    }

    static JPanel marca() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MARCA);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 36, 40, 36));
        panel.setPreferredSize(new Dimension(320, 0));

        JLabel titulo = new JLabel("Clínica");
        titulo.setFont(TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel texto = new JLabel("<html>Prestaciones, órdenes<br>y seguimiento en un<br>solo lugar.</html>");
        texto.setFont(SUBTITULO);
        texto.setForeground(new Color(210, 236, 236));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        panel.add(titulo, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(16, 0, 0, 0);
        panel.add(texto, gbc);
        return panel;
    }
}
