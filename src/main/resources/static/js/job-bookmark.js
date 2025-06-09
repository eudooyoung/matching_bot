// 전체 선택/해제
function toggleAll(source) {
    const checkboxes = document.querySelectorAll('input[name="jobIds"]');
    checkboxes.forEach(cb => cb.checked = source.checked);
}

// 선택된 북마크 삭제
function deleteSelected() {
    const checked = document.querySelectorAll('input[name="jobIds"]:checked');
    if (checked.length === 0) {
        alert("삭제할 채용공고를 선택하세요.");
        return;
    }

    if (!confirm("선택한 채용공고를 관심 목록에서 삭제하시겠습니까?")) {
        return;
    }

    const ids = Array.from(checked).map(cb => cb.value);

    fetch('/member/api/member/job-bookmark/delete', {
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
    const jobId = button.getAttribute('data-job-id');

    if (!confirm("해당 채용공고를 관심 목록에서 제거하시겠습니까?")) return;

    fetch(`/member/api/member/job-bookmark/${jobId}`, {
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