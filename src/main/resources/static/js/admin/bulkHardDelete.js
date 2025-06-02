async function bulkHardDelete(url) {
    const selected = Array.from(document.querySelectorAll(".check-row:checked")).map(cb => cb.value);

    if (selected.length === 0) {
        alert("삭제할 항목을 선택하세요.");
        return;
    }

    if (!confirm("선택한 항목들을 완전히 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;

    try {
        const response = await fetchWithAuth(url, {
            method: "DELETE",
            body: JSON.stringify({ ids: selected })
        });

        if (!response.ok) {
            const errMsg = await response.text();
            alert(`삭제 실패: ${errMsg}`);
        }
    } catch (err) {
        alert("네트워크 오류로 삭제 실패");
        console.error(err);
    }

    location.reload();
}

document.addEventListener("DOMContentLoaded", () => {
    const checkAll = document.getElementById("check-all");
    const checkboxes = document.querySelectorAll(".check-row");

    if (!checkAll || checkboxes.length === 0) return;

    checkAll.addEventListener("change", () => {
        checkboxes.forEach(cb => {
            cb.checked = checkAll.checked;
        });
    });

    checkboxes.forEach(cb => {
        cb.addEventListener("change", () => {
            if (!cb.checked) {
                checkAll.checked = false;
            } else if (Array.from(checkboxes).every(c => c.checked)) {
                checkAll.checked = true;
            }
        });
    });
});