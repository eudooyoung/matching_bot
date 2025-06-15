/**
 * 비활성화 또는 복구 처리용 Bulk Action (REST 방식)
 * @param {'DELETE'|'PATCH'} method - HTTP method
 * @param {string} url - 예: '/admin/members/bulk'
 */
async function bulkSoftAction(method, url) {
    const selected = Array.from(document.querySelectorAll("input[name='checkedIds']:checked"));
    if (selected.length === 0) {
        alert("선택된 항목이 없습니다.");
        return;
    }

    const statuses = new Set(selected.map(cb => cb.dataset.status));
    if (statuses.size > 1) {
        alert("서로 다른 상태의 항목이 포함되어 있어 처리할 수 없습니다.");
        return;
    }

    const invalid = selected.filter(cb => {
        const s = cb.dataset.status;
        return (method === "DELETE" && s === "N") || (method === "PATCH" && s === "Y");
    });
    if (invalid.length > 0) {
        alert("이미 처리된 항목이 포함되어 있습니다.");
        return;
    }

    const msg = method === "DELETE" ? "비활성화" : "복구";
    if (!confirm(`선택한 항목을 ${msg}하시겠습니까?`)) return;

    const ids = selected.map(cb => cb.value);
    try {
        const res = await fetchWithAuth(url, {
            method,
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ids})
        });
        if (!res.ok) throw new Error("서버 오류");
        location.reload();
    } catch (e) {
        alert(`요청 실패: ${e.message}`);
    }
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
