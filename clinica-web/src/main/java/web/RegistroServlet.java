package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registro")
public class RegistroServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { vista("registro", req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            servicio().registrar(req.getParameter("tipo"), req.getParameter("email"),
                    req.getParameter("nombre"), req.getParameter("password"), req.getParameter("detalle"));
            req.getSession().setAttribute("flash", "Usuario registrado. Ya podés iniciar sesión.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (ServicioException e) {
            req.setAttribute("error", mensaje(e));
            vista("registro", req, resp);
        }
    }
}
