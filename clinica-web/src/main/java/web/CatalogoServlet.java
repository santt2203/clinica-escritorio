package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import cliente.soap.Prestacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/catalogo")
public class CatalogoServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object flash = req.getSession().getAttribute("flash");
        Object flashError = req.getSession().getAttribute("flashError");
        if (flash != null) { req.setAttribute("mensaje", flash); req.getSession().removeAttribute("flash"); }
        if (flashError != null) { req.setAttribute("error", flashError); req.getSession().removeAttribute("flashError"); }
        String texto = req.getParameter("q");
        try {
            List<Prestacion> prestaciones = new ArrayList<>(servicio().catalogo(texto));
            String orden = req.getParameter("orden");
            if ("precio".equals(orden))
                prestaciones.sort(Comparator.comparingDouble(Prestacion::getPrecio));
            else
                prestaciones.sort(Comparator.comparing(Prestacion::getNombre, String.CASE_INSENSITIVE_ORDER));
            req.setAttribute("prestaciones", prestaciones);
            req.setAttribute("q", texto);
        } catch (ServicioException e) {
            req.setAttribute("error", mensaje(e));
        }
        vista("catalogo", req, resp);
    }
}
