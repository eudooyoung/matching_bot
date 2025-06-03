function loadImageWithSpinner(imgId, spinnerId, url) {
    const img = document.getElementById(imgId);
    const spinner = document.getElementById(spinnerId);
    if (!img || !spinner) return;

    img.onload = () => {
        spinner.style.display = "none";
        img.style.display = "block";
    };

    img.onerror = () => {
        spinner.innerHTML = "<p>불러오기 실패</p>";
    };

    img.src = url;
}
