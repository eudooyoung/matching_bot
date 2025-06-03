document.addEventListener("DOMContentLoaded", async function () {
    const imgId = "reportImage";
    const spinnerId = "reportImageSpinner";
    const img = document.getElementById(imgId);
    const companyId = img?.dataset.companyId;
    if (!companyId || !img) return;

    try {
        const response = await fetchWithAuth(`/attached/summary-image-path/${companyId}`);
        if (response.ok) {
            const url = await response.text();
            loadImageWithSpinner(imgId, spinnerId, url);
        } else {
            document.getElementById(spinnerId).innerHTML = "<p>이미지 경로 요청 실패</p>";
        }
    } catch (err) {
        console.error("이미지 로딩 오류", err);
        document.getElementById(spinnerId).innerHTML = "<p>이미지 로딩 오류</p>";
    }
});
