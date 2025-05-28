/**
 * 공통 bulkAction 요청 함수
 * @param {string} actionType - 'DELETE' | 'RESTORE' 등 액션 타입
 * @param {string} url - 요청할 서버 URL (예: '/admin/bulk/members')
 * @param {string} paramName - 체크박스 name 속성 (예: 'checkedIds')
 */

function setBulkAction(actionType, url, paramName = "checkedIds") {
    const selectedCheckboxes = Array.from(document.querySelectorAll(`input[name="${paramName}"]:checked`));

    if (selectedCheckboxes.length === 0) {
        alert("선택된 항목이 없습니다.");
        return;
    }

    const statuses = new Set(selectedCheckboxes.map(cb => cb.dataset.status));

    if (statuses.size > 1) {
        alert("서로 다른 상태의 회원이 포함되어 있어 처리할 수 없습니다.");
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

    if (!confirm(`선택한 항목을 ${actionType === 'DELETE' ? '삭제' : '복구'}하시겠습니까?`)) {
        return;
    }

    const form = document.createElement("form");
    form.method = "post";
    form.action = url;

    selectedCheckboxes.forEach(cb => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = paramName;
        input.value = cb.value;
        form.appendChild(input);
    });

    const actionInput = document.createElement("input");
    actionInput.type = "hidden";
    actionInput.name = "actionType";
    actionInput.value = actionType;
    form.appendChild(actionInput);

    document.body.appendChild(form);
    form.submit();
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