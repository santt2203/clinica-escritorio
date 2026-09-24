<%@ include file="fragments/header.jspf" %>
<c:if test="${not empty prestacion}">
<article class="form-card">
    <span class="badge"><c:out value="${prestacion.tipo}"/></span><h1><c:out value="${prestacion.nombre}"/></h1>
    <dl class="detail-list"><dt>Precio</dt><dd><fmt:formatNumber value="${prestacion.precio}" type="currency" currencySymbol="$ "/></dd>
        <dt>Franja</dt><dd><c:out value="${prestacion.franja}"/></dd>
        <c:if test="${prestacion.tipo == 'ESTUDIO'}"><dt>Duración</dt><dd>${prestacion.duracionMinutos} minutos</dd></c:if>
        <c:if test="${prestacion.tipo == 'TERAPIA'}"><dt>Sesiones</dt><dd>${prestacion.cantidadSesiones}</dd><dt>Derivación</dt><dd>${prestacion.isRequiereDerivacion() ? 'Sí' : 'No'}</dd></c:if>
    </dl>
</article>
</c:if>
<%@ include file="fragments/footer.jspf" %>
