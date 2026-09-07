package presentacion;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;  import javax.swing.JList;
import javax.swing.JOptionPane;

import datatypes.DtEstudio;
import datatypes.DtPrestacion;
import datatypes.DtTerapia;
import datatypes.Franja;
import excepciones.AccesoDenegadoException;
import excepciones.PrestacionRepetidaException;
import interfaces.IControlador;

public class ModificarPrestacion extends VentanaInterna {
    private static final long serialVersionUID = 1L;
    private final JComboBox<DtPrestacion> campoPrestacion = new JComboBox<>();
    private final JTextField campoTipo = new JTextField();
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoPrecio = new JTextField();
    private final JComboBox <Franja> campoFranja = new JComboBox<>(Franja.values());
    private final JSpinner campoDuracion= new JSpinner(new SpinnerNumberModel(30, 1, 1440, 1));
    private final JCheckBox campoRequiereDerivacion = new JCheckBox("Requiere derivación");
    private final JSpinner campoCantidadSesiones =new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    
    public ModificarPrestacion(IControlador icon){
        super(icon,"ModificarPrestacion",480,400);
        agregarCampo("Prestación", campoPrestacion);
        agregarCampo("Tipo", campoTipo);
        agregarCampo("Nombre", campoNombre);
        agregarCampo("Precio", campoPrecio);
        agregarCampo("Franja", campoFranja);
        agregarCampo("Duración (minutos)", campoDuracion);
        agregarCampo("Derivación", campoRequiereDerivacion);
        agregarCampo("Cantidad de sesiones", campoCantidadSesiones);
        agregarBoton("Cancelar", this::cerrar);
        agregarBoton("Guardar", this::guardar);
        
    }
    private void guardar(){
        
    }
}
