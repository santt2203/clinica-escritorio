package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/prestacion")
public class DetalleServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try { req.setAttribute("prestacion", servicio().prestacion(id(req))); }
        catch (Exception e) { req.setAttribute("error", mensaje(e)); }
        vista("detalle", req, resp);
    }
}
