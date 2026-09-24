package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import cliente.soap.Prestacion;

@WebServlet("/medico/prestaciones/nueva")
public class PrestacionFormServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        vista("prestacion-form", req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            servicio().agregarPrestacion(token(req), desdeFormulario(req));
            req.getSession().setAttribute("flash", "Prestación agregada.");
            resp.sendRedirect(req.getContextPath() + "/catalogo");
        } catch (Exception e) {
            req.setAttribute("error", mensaje(e));
            vista("prestacion-form", req, resp);
        }
    }

    private Prestacion desdeFormulario(HttpServletRequest req) {
        Prestacion p = new Prestacion();
        p.setTipo(req.getParameter("tipo"));
        p.setNombre(req.getParameter("nombre"));
        p.setPrecio(decimal(req.getParameter("precio"), "precio"));
        p.setFranja(req.getParameter("franja"));
        if ("ESTUDIO".equals(req.getParameter("tipo")))
            p.setDuracionMinutos(entero(req.getParameter("duracionMinutos"), "duración"));
        else {
            p.setCantidadSesiones(entero(req.getParameter("cantidadSesiones"), "cantidad de sesiones"));
            p.setRequiereDerivacion(req.getParameter("requiereDerivacion") != null);
        }
        return p;
    }
}
