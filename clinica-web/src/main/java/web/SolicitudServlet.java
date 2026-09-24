package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cliente.soap.CantidadSoap;
import cliente.soap.Prestacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/paciente/solicitud")
public class SolicitudServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String ATRIBUTO = "solicitud";

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { mostrar(req, resp); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String accion = req.getParameter("accion");
            if ("confirmar".equals(accion)) {
                List<CantidadSoap> lineas = cantidades(req).entrySet().stream().map(e -> {
                    CantidadSoap linea = new CantidadSoap();
                    linea.setIdPrestacion(e.getKey()); linea.setCantidad(e.getValue()); return linea;
                }).toList();
                servicio().confirmarOrden(token(req), lineas);
                req.getSession().removeAttribute(ATRIBUTO);
                req.getSession().setAttribute("flash", "Orden médica confirmada.");
                resp.sendRedirect(req.getContextPath() + "/paciente/ordenes");
                return;
            }
            long id = id(req);
            if ("quitar".equals(accion)) cantidades(req).remove(id);
            else {
                servicio().prestacion(id); // valida el identificador en el servidor central
                int cantidad = entero(req.getParameter("cantidad"), "cantidad");
                cantidades(req).merge(id, cantidad, Integer::sum);
            }
        } catch (Exception e) {
            req.setAttribute("error", mensaje(e));
        }
        mostrar(req, resp);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> cantidades(HttpServletRequest req) {
        Map<Long, Integer> mapa = (Map<Long, Integer>) req.getSession().getAttribute(ATRIBUTO);
        if (mapa == null) {
            mapa = new LinkedHashMap<>();
            req.getSession().setAttribute(ATRIBUTO, mapa);
        }
        return mapa;
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<SolicitudItem> items = new ArrayList<>();
        double total = 0;
        try {
            for (Map.Entry<Long, Integer> entrada : cantidades(req).entrySet()) {
                Prestacion p = servicio().prestacion(entrada.getKey());
                SolicitudItem item = new SolicitudItem(p, entrada.getValue());
                items.add(item); total += item.getSubtotal();
            }
        } catch (ServicioException e) { req.setAttribute("error", mensaje(e)); }
        req.setAttribute("items", items); req.setAttribute("total", total);
        vista("solicitud", req, resp);
    }
}
