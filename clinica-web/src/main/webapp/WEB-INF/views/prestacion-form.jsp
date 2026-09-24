<%@ include file="fragments/header.jspf" %>
<section class="form-card">
    <p class="eyebrow">Administración médica</p><h1>${editar ? 'Modificar' : 'Agregar'} prestación</h1>
    <form method="post" class="stack" id="prestacion-form">
        <c:if test="${editar}"><input type="hidden" name="id" value="${prestacion.id}"></c:if>
        <label>Tipo<select name="tipo" id="tipo-prestacion" ${editar ? 'disabled' : ''}>
            <option value="ESTUDIO" ${prestacion.tipo == 'ESTUDIO' ? 'selected' : ''}>Estudio</option>
            <option value="TERAPIA" ${prestacion.tipo == 'TERAPIA' ? 'selected' : ''}>Terapia</option>
        </select></label>
        <c:if test="${editar}"><input type="hidden" name="tipo" value="${prestacion.tipo}"></c:if>
        <label>Nombre<input name="nombre" value="${prestacion.nombre}" required></label>
        <label>Precio<input type="number" name="precio" value="${prestacion.precio}" step="0.01" min="0.01" required></label>
        <label>Franja<select name="franja"><option value="MANANA">Mañana</option><option value="TARDE">Tarde</option><option value="NOCHE">Noche</option></select></label>
        <div id="campos-estudio"><label>Duración (minutos)<input type="number" name="duracionMinutos" value="${prestacion.duracionMinutos}" min="1"></label></div>
        <div id="campos-terapia"><label>Cantidad de sesiones<input type="number" name="cantidadSesiones" value="${prestacion.cantidadSesiones}" min="1"></label>
            <label class="check"><input type="checkbox" name="requiereDerivacion" ${not empty prestacion and prestacion.isRequiereDerivacion() ? 'checked' : ''}> Requiere derivación</label></div>
        <button type="submit">Guardar</button>
    </form>
</section>
<%@ include file="fragments/footer.jspf" %>
