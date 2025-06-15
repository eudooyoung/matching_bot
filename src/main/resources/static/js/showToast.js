function showToast(message) {
    const el = document.getElementById("globalToast");
    if (!el) return;

    el.innerText = message;
    el.className = `toast`;
    el.style.display = "block";

    setTimeout(() => {
        el.style.display = "none";
    }, 3000);
}