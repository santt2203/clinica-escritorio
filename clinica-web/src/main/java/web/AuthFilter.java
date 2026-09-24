package web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Autenticación y autorización web centralizadas para todas las rutas privadas. */
@WebFilter(urlPatterns = { "/medico/*", "/paciente/*" })
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse salida = (HttpServletResponse) response;
        HttpSession sesion = http.getSession(false);
        String rol = sesion == null ? null : (String) sesion.getAttribute("rol");
        if (rol == null) {
            salida.sendRedirect(http.getContextPath() + "/login?requerido=1");
            return;
        }
        String ruta = http.getRequestURI().substring(http.getContextPath().length());
        String requerido = ruta.startsWith("/medico/") ? "MEDICO" : "PACIENTE";
        if (!requerido.equals(rol)) {
            salida.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No tenés permiso para acceder a esta función");
            return;
        }
        chain.doFilter(request, response);
    }
}
