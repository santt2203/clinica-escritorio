<%@ include file="fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Paciente</p><h1>Historial de órdenes</h1></div></section>
<div class="list"><c:forEach items="${ordenes}" var="o"><article class="card order"><div class="card-title"><h2>Orden #${o.id}</h2><time><c:out value="${o.fecha}"/></time></div>
    <ul><c:forEach items="${o.lineas}" var="l"><li><c:out value="${l.nombrePrestacion}"/> · ${l.cantidad} × <fmt:formatNumber value="${l.precioUnitario}" type="currency" currencySymbol="$ "/> = <fmt:formatNumber value="${l.subtotal}" type="currency" currencySymbol="$ "/></li></c:forEach></ul>
    <div class="total"><span>Total</span><strong><fmt:formatNumber value="${o.total}" type="currency" currencySymbol="$ "/></strong></div>
</article></c:forEach></div>
<c:if test="${empty ordenes and empty error}"><p class="empty">Todavía no hay órdenes confirmadas.</p></c:if>
<%@ include file="fragments/footer.jspf" %>
