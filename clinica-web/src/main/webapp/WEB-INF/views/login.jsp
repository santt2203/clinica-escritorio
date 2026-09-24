<%@ include file="fragments/header.jspf" %>
<section class="form-card narrow">
    <p class="eyebrow">Acceso</p><h1>Iniciar sesión</h1>
    <c:if test="${param.requerido == '1'}"><div class="alert error">Iniciá sesión para continuar.</div></c:if>
    <form method="post" class="stack">
        <label>Email<input type="email" name="email" required autocomplete="email"></label>
        <label>Contraseña<input type="password" name="password" required autocomplete="current-password"></label>
        <button type="submit">Ingresar</button>
    </form>
</section>
<%@ include file="fragments/footer.jspf" %>
