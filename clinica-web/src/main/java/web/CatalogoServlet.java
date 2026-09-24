package web;

import java.io.IOException;
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
        try {
            req.setAttribute("prestaciones", servicio().catalogo(null));
        } catch (ServicioException e) {
            req.setAttribute("error", mensaje(e));
        }
        vista("catalogo", req, resp);
    }
}
