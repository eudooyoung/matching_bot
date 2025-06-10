// 전체 선택/해제
function toggleAll(source) {
    const checkboxes = document.querySelectorAll('input[name="companyIds"]');
    checkboxes.forEach(cb => cb.checked = source.checked);
}

// 선택된 북마크 삭제
function deleteSelected() {
    const checked = document.querySelectorAll('input[name="companyIds"]:checked');
    if (checked.length === 0) {
        alert("삭제할 기업을 선택하세요.");
        return;
    }

    if (!confirm("선택한 기업을 관심 목록에서 삭제하시겠습니까?")) {
        return;
    }

    const ids = Array.from(checked).map(cb => cb.value);

    fetch('/member/api/member/company-bookmark/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(ids)
    })
        .then(response => {
            if (response.ok) {
                alert("삭제되었습니다.");
                location.reload();
            } else {
                alert("삭제에 실패했습니다.");
            }
        });
}

// 즐겨찾기 해제 (★ 클릭)
function deleteBookmark(button) {
    const companyId = button.getAttribute('data-company-id');

    if (!confirm("해당 기업을 관심 목록에서 제거하시겠습니까?")) return;

    fetch(`/member/api/member/company-bookmark/${companyId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert("북마크가 해제되었습니다.");
                location.reload();
            } else {
                alert("실패했습니다.");
            }
        });
}