package web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected SoapClient servicio() { return SoapClient.obtener(getServletContext()); }
    protected String token(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("token");
    }
    protected void vista(String nombre, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + nombre + ".jsp").forward(request, response);
    }
    protected Long id(HttpServletRequest request) {
        try { return Long.valueOf(request.getParameter("id")); }
        catch (RuntimeException e) { throw new IllegalArgumentException("El identificador no es válido"); }
    }
    protected int entero(String valor, String campo) {
        try { return Integer.parseInt(valor); }
        catch (RuntimeException e) { throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero"); }
    }
    protected double decimal(String valor, String campo) {
        try { return Double.parseDouble(valor.replace(',', '.')); }
        catch (RuntimeException e) { throw new IllegalArgumentException("El campo " + campo + " debe ser un número"); }
    }
    protected String mensaje(Exception e) {
        return e.getMessage() == null ? "No se pudo completar la operación" : e.getMessage();
    }
}
