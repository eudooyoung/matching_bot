// 선택된 이력서 일괄 삭제 요청
function deleteSelected() {
    const checked = document.querySelectorAll('input[name="resumeIds"]:checked');
    const ids = Array.from(checked).map(cb => parseInt(cb.value));

    const deleteBtn = document.querySelector('.delete-button');
    const companyId = deleteBtn ? deleteBtn.dataset.companyId : null;

    if (!companyId) {
        alert('회사 ID를 불러올 수 없습니다.');
        return;
    }

    if (ids.length === 0) {
        alert('삭제할 이력서를 선택해주세요.');
        return;
    }

    if (!confirm('선택한 이력서를 삭제하시겠습니까?')) return;

    fetch(`/job/resume-bookmark/bulk/company/${companyId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(ids)
    })
        .then(response => {
            if (response.ok) {
                alert('삭제가 완료되었습니다.');
                location.reload();
            } else {
                return response.text().then(msg => {
                    console.error('삭제 실패 응답 내용:', msg);
                    alert('삭제 중 오류가 발생했습니다: ' + msg);
                });
            }
        })
        .catch(error => {
            console.error('fetch 예외 발생:', error);
            alert('요청 처리 중 예외가 발생했습니다: ' + error.message);
        });
}

// 개별 즐겨찾기 삭제 요청
function deleteBookmark(button) {
    const resumeId = button.getAttribute('data-resume-id');
    const companyId = button.getAttribute('data-company-id');

    fetch(`/job/resume-bookmark/${resumeId}/company/${companyId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert('삭제가 완료되었습니다.');
                button.closest('tr').remove();
            } else {
                return response.text().then(msg => {
                    console.error('삭제 실패 응답 내용:', msg);
                    alert('삭제 중 오류가 발생했습니다: ' + msg);
                });
            }
        })
        .catch(error => {
            console.error('fetch 예외 발생:', error);
            alert('요청 처리 중 예외가 발생했습니다: ' + error.message);
        });
}

// 전체 선택 토글
function toggleAll(masterCheckbox) {
    const checkboxes = document.querySelectorAll('input[name="resumeIds"]');
    checkboxes.forEach(cb => cb.checked = masterCheckbox.checked);
}