package interfaces;

import logica.Controlador;

/**
 * Singleton. La presentacion pide el controlador aca y nunca hace "new Controlador()",
 * asi la logica puede cambiar sin tocar la presentacion.
 */
public class Fabrica {

    private static Fabrica instancia = null;

    private Fabrica() {
    }

    public static Fabrica getInstancia() {
        if (instancia == null)
            instancia = new Fabrica();
        return instancia;
    }

    public IControlador getIControlador() {
        return new Controlador();
    }
}
