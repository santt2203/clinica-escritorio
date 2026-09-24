package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/paciente/seguidas")
public class SeguidasServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { mostrar(req, resp, false); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean ajax = "fetch".equals(req.getHeader("X-Requested-With"));
        try {
            if ("quitar".equals(req.getParameter("accion")))
                servicio().quitarSeguido(token(req), id(req));
            else
                servicio().agregarSeguido(token(req), id(req));
            req.setAttribute("mensaje", "Lista actualizada.");
        } catch (Exception e) {
            req.setAttribute("error", mensaje(e));
        }
        mostrar(req, resp, ajax);
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, boolean fragmento)
            throws ServletException, IOException {
        try { req.setAttribute("seguidos", servicio().seguidos(token(req))); }
        catch (ServicioException e) { req.setAttribute("error", mensaje(e)); }
        if (fragmento)
            req.getRequestDispatcher("/WEB-INF/views/fragments/seguidas.jsp").forward(req, resp);
        else
            vista("seguidas", req, resp);
    }
}
