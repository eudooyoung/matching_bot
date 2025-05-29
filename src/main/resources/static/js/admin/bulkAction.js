/**
 * 공통 bulkAction 요청 함수
 * @param {string} actionType - 'DELETE' | 'RESTORE' 등 액션 타입
 * @param {string} url - 요청할 서버 URL (예: '/admin/bulk/members')
 * @param {string} paramName - 체크박스 name 속성 (예: 'checkedIds')
 */

async function setBulkAction(actionType, url, paramName = "checkedIds") {
    const selectedCheckboxes = Array.from(document.querySelectorAll(`input[name="${paramName}"]:checked`));

    if (selectedCheckboxes.length === 0) {
        alert("선택된 항목이 없습니다.");
        return;
    }

    const statuses = new Set(selectedCheckboxes.map(cb => cb.dataset.status));

    if (statuses.size > 1) {
        alert("서로 다른 상태의 항목이 포함되어 있어 처리할 수 없습니다.");
        return;
    }

    const invalid = selectedCheckboxes.filter(cb => {
        const status = cb.dataset.status;
        return (actionType === "DELETE" && status === "N") ||
            (actionType === "RESTORE" && status === "Y");
    });

    if (invalid.length > 0) {
        alert("이미 처리된 항목이 포함되어 있습니다.");
        return;
    }

    const confirmMsg = actionType === "DELETE" ? "삭제" : "복구";
    if (!confirm(`선택한 항목을 ${confirmMsg}하시겠습니까?`)) {
        return;
    }

    const ids = selectedCheckboxes.map(cb => cb.value);

    try {
        const response = await fetchWithAuth(url, {
            method: "POST",
            body: JSON.stringify({[paramName]: ids, actionType})
        });

        if (!response.ok) {
            throw new Error("처리 중 오류 발생");
        }

        location.reload();
    } catch (err) {
        alert("요청 실패: " + err.message);
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