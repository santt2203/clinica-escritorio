<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty error}"><div class="alert error" role="alert"><c:out value="${error}"/></div></c:if>
<c:if test="${not empty mensaje}"><div class="alert success" role="status"><c:out value="${mensaje}"/></div></c:if>
<div class="card-grid">
<c:forEach items="${seguidos}" var="s"><article class="card"><h2><c:out value="${s.prestacion.nombre}"/></h2>
    <p>Seguida desde <c:out value="${s.fecha}"/></p>
    <form action="${pageContext.request.contextPath}/paciente/seguidas" method="post" class="ajax-follow">
        <input type="hidden" name="id" value="${s.prestacion.id}"><input type="hidden" name="accion" value="quitar"><button class="danger" type="submit">Dejar de seguir</button>
    </form></article></c:forEach>
</div>
<c:if test="${empty seguidos and empty error}"><p class="empty">Todavía no seguís prestaciones.</p></c:if>
