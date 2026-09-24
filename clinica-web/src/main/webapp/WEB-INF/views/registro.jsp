<%@ include file="fragments/header.jspf" %>
<section class="form-card narrow">
    <p class="eyebrow">Nueva cuenta</p><h1>Registrarse</h1>
    <form method="post" class="stack" id="registro-form">
        <label>Tipo de usuario<select name="tipo" id="tipo-usuario"><option value="PACIENTE">Paciente</option><option value="MEDICO">Médico</option></select></label>
        <label>Email<input type="email" name="email" required autocomplete="email"></label>
        <label>Nombre<input type="text" name="nombre" required></label>
        <label>Contraseña<input type="password" name="password" required minlength="6" autocomplete="new-password"></label>
        <label><span id="detalle-label">Mutualista</span><input type="text" name="detalle" required></label>
        <button type="submit">Crear cuenta</button>
    </form>
</section>
<%@ include file="fragments/footer.jspf" %>
