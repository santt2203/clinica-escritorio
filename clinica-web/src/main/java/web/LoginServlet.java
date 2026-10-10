package web;

import java.io.IOException;
import cliente.soap.SesionSoap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object flash = req.getSession().getAttribute("flash");
        if (flash != null) { req.setAttribute("mensaje", flash); req.getSession().removeAttribute("flash"); }
        vista("login", req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            SesionSoap sesionSoap = servicio().iniciarSesion(req.getParameter("email"), req.getParameter("password"));
            HttpSession sesion = req.getSession();
            sesion.setAttribute("token", sesionSoap.getToken());
            sesion.setAttribute("usuario", sesionSoap.getUsuario());
            sesion.setAttribute("rol", sesionSoap.getUsuario().getTipo());
            resp.sendRedirect(req.getContextPath() + "/catalogo");
        } catch (ServicioException e) {
            req.setAttribute("error", mensaje(e));
            vista("login", req, resp);
        }
    }
}
