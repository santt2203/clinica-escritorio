package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/paciente/ordenes")
public class OrdenesServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object flash = req.getSession().getAttribute("flash");
        if (flash != null) { req.setAttribute("mensaje", flash); req.getSession().removeAttribute("flash"); }
        try { req.setAttribute("ordenes", servicio().ordenes(token(req))); }
        catch (ServicioException e) { req.setAttribute("error", mensaje(e)); }
        vista("ordenes", req, resp);
    }
}
