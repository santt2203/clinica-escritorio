package web;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = token(req);
        if (token != null) servicio().cerrarSesion(token);
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/catalogo");
    }
}
