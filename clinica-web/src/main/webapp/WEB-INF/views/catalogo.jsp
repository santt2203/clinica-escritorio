<%@ include file="fragments/header.jspf" %>
<section class="page-heading">
    <div><p class="eyebrow">Prestaciones</p><h1>Catálogo</h1></div>
    <c:if test="${sessionScope.rol == 'MEDICO'}">
        <a class="button" href="${pageContext.request.contextPath}/medico/prestaciones/nueva">Agregar prestación</a>
    </c:if>
</section>

<form class="toolbar" action="${pageContext.request.contextPath}/catalogo" method="get">
    <label>Buscar por nombre
        <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Ej.: análisis">
    </label>
    <button type="submit">Buscar</button>
</form>

<div id="catalogo" class="card-grid">
    <c:forEach items="${prestaciones}" var="p">
        <article class="card prestacion">
            <div class="card-title"><span class="badge"><c:out value="${p.tipo}"/></span><h2><c:out value="${p.nombre}"/></h2></div>
            <dl><dt>Precio</dt><dd><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$ "/></dd>
                <dt>Franja</dt><dd><c:out value="${p.franja}"/></dd></dl>
            <div class="actions">
                <a class="button secondary" href="${pageContext.request.contextPath}/prestacion?id=${p.id}">Ver detalle</a>
                <c:if test="${sessionScope.rol == 'MEDICO'}">
                    <a class="button secondary" href="${pageContext.request.contextPath}/medico/prestaciones/editar?id=${p.id}">Editar</a>
                    <form action="${pageContext.request.contextPath}/medico/prestaciones/eliminar" method="post" class="inline">
                        <input type="hidden" name="id" value="${p.id}"><button class="danger" type="submit">Eliminar</button>
                    </form>
                </c:if>
            </div>
        </article>
    </c:forEach>
</div>
<c:if test="${empty prestaciones and empty error}"><p class="empty">No se encontraron prestaciones.</p></c:if>
<%@ include file="fragments/footer.jspf" %>
