document.addEventListener("DOMContentLoaded", async function () {
    const img = document.getElementById("reportImage");
    if (!img) return;

    const companyId = img.dataset.companyId;
    if (!companyId) return;

    try {
        const res = await fetchWithAuth(`/api/v1/attached/report-image-path/${companyId}`);
        if (res.ok) {
            const url = await res.text();
            console.log(url);
            img.src = url;
        } else {
            img.src = "/img/fallback.jpg";
        }
    } catch (e) {
        console.error("이미지 로드 오류:", e);
        img.src = "/img/fallback.jpg";
    }
});
