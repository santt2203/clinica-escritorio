<%@ include file="fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Paciente</p><h1>Mi solicitud</h1></div><a class="button secondary" href="${pageContext.request.contextPath}/catalogo">Agregar prestaciones</a></section>
<div class="list">
<c:forEach items="${items}" var="item"><article class="list-row"><div><h2><c:out value="${item.prestacion.nombre}"/></h2><p>${item.cantidad} × <fmt:formatNumber value="${item.prestacion.precio}" type="currency" currencySymbol="$ "/></p></div>
    <strong><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="$ "/></strong>
    <form method="post"><input type="hidden" name="accion" value="quitar"><input type="hidden" name="id" value="${item.prestacion.id}"><button class="danger">Quitar</button></form></article></c:forEach>
</div>
<c:choose><c:when test="${empty items}"><p class="empty">La solicitud está vacía.</p></c:when><c:otherwise>
    <div class="total"><span>Total calculado por precios del servicio</span><strong><fmt:formatNumber value="${total}" type="currency" currencySymbol="$ "/></strong></div>
    <form method="post"><input type="hidden" name="accion" value="confirmar"><button type="submit">Confirmar orden médica</button></form>
</c:otherwise></c:choose>
<%@ include file="fragments/footer.jspf" %>
