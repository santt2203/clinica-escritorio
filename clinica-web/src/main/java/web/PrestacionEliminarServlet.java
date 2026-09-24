package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/medico/prestaciones/eliminar")
public class PrestacionEliminarServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            servicio().eliminarPrestacion(token(req), id(req));
            req.getSession().setAttribute("flash", "Prestación eliminada.");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", mensaje(e));
        }
        resp.sendRedirect(req.getContextPath() + "/catalogo");
    }
}
