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
});
