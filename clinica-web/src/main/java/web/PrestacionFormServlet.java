package web;

import java.io.IOException;
import cliente.soap.Prestacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/medico/prestaciones/nueva", "/medico/prestaciones/editar" })
public class PrestacionFormServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean editar = req.getServletPath().endsWith("/editar");
        req.setAttribute("editar", editar);
        if (editar) {
            try { req.setAttribute("prestacion", servicio().prestacion(id(req))); }
            catch (Exception e) { req.setAttribute("error", mensaje(e)); }
        }
        vista("prestacion-form", req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean editar = req.getServletPath().endsWith("/editar");
        try {
            Prestacion p = desdeFormulario(req, editar);
            if (editar) servicio().modificarPrestacion(token(req), p);
            else servicio().agregarPrestacion(token(req), p);
            req.getSession().setAttribute("flash", editar ? "Prestación modificada." : "Prestación agregada.");
            resp.sendRedirect(req.getContextPath() + "/catalogo");
        } catch (Exception e) {
            req.setAttribute("editar", editar);
            req.setAttribute("error", mensaje(e));
            vista("prestacion-form", req, resp);
        }
    }

    private Prestacion desdeFormulario(HttpServletRequest req, boolean editar) {
        Prestacion p = new Prestacion();
        if (editar) p.setId(id(req));
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
