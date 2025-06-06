function bindFullAddress(formId, addressId, detailId, hiddenId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener("submit", function () {
        const address = document.getElementById(addressId)?.value.trim() || "";
        const detail = document.getElementById(detailId)?.value.trim() || "";
        const full = detail ? `${address} ${detail}` : address;
        const hidden = document.getElementById(hiddenId);
        if (hidden) hidden.value = full;
    });
}

// 전역에 노출
window.bindFullAddress = bindFullAddress;
