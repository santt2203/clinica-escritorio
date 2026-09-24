document.addEventListener("DOMContentLoaded", () => {
    const order = document.querySelector("#orden-catalogo");
    const catalog = document.querySelector("#catalogo");
    order?.addEventListener("change", () => {
        const cards = [...catalog.querySelectorAll(".prestacion")];
        cards.sort((a, b) => order.value === "precio"
            ? Number(a.dataset.price) - Number(b.dataset.price)
            : a.dataset.name.localeCompare(b.dataset.name, "es", { sensitivity: "base" }));
        cards.forEach(card => catalog.append(card));
    });

    document.querySelectorAll(".confirm-delete").forEach(form => form.addEventListener("submit", event => {
        if (!confirm("¿Eliminar esta prestación?")) event.preventDefault();
    }));

    const userType = document.querySelector("#tipo-usuario");
    const updateUserLabel = () => {
        const label = document.querySelector("#detalle-label");
        if (label) label.textContent = userType.value === "MEDICO" ? "Especialidad" : "Mutualista";
    };
    userType?.addEventListener("change", updateUserLabel); updateUserLabel();

    const serviceType = document.querySelector("#tipo-prestacion");
    const updateServiceFields = () => {
        if (!serviceType) return;
        const study = document.querySelector("#campos-estudio");
        const therapy = document.querySelector("#campos-terapia");
        const isStudy = serviceType.value === "ESTUDIO";
        study.hidden = !isStudy; therapy.hidden = isStudy;
        study.querySelector("input").required = isStudy;
        therapy.querySelector("input[type=number]").required = !isStudy;
    };
    serviceType?.addEventListener("change", updateServiceFields); updateServiceFields();

    document.addEventListener("submit", async event => {
        const form = event.target.closest(".ajax-follow");
        if (!form) return;
        event.preventDefault();
        try {
            const response = await fetch(form.action, {
                method: "POST", body: new URLSearchParams(new FormData(form)),
                headers: { "X-Requested-With": "fetch" }
            });
            if (!response.ok) throw new Error("No se pudo actualizar la lista");
            const html = await response.text();
            const list = document.querySelector("#seguidas");
            if (list) list.innerHTML = html;
            else document.querySelector("#ajax-message").innerHTML = '<div class="alert success">Lista de seguidas actualizada.</div>';
        } catch (error) {
            const target = document.querySelector("#ajax-message") || document.querySelector("#seguidas");
            if (target) target.innerHTML = '<div class="alert error">' + error.message + '</div>';
        }
    });
});
