function loadDaumPostcode(targetId) {
    new daum.Postcode({
        oncomplete: function(data) {
            const input = document.getElementById(targetId);
            if (input) input.value = data.address;
        }
    }).open();
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".address-btn[data-target]").forEach(btn => {
        btn.addEventListener("click", () => {
            const targetId = btn.dataset.target;
            loadDaumPostcode(targetId);
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const addressInput = document.getElementById("addressRegion");

    if (addressInput) {
        addressInput.addEventListener("click", () => {
            const btn = document.querySelector(`.address-btn[data-target="addressRegion"]`);
            if (btn) btn.click();
        });
    }
});