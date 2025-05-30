// 관심 이력서 페이지에서 삭제 및 즐겨찾기 해제 기능을 담당하는 JS

// 선택된 이력서 일괄 삭제 요청
function deleteSelected() {
    const checked = document.querySelectorAll('input[name="resumeIds"]:checked');
    const ids = Array.from(checked).map(cb => cb.value);

    if (ids.length === 0) {
        alert('삭제할 이력서를 선택해주세요.');
        return;
    }

    if (!confirm('선택한 이력서를 삭제하시겠습니까?')) return;

    fetch('/resume-bookmarks', {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(ids)
    }).then(response => {
        if (response.ok) {
            alert('삭제가 완료되었습니다.');
            location.reload();
        } else {
            alert('삭제 중 오류가 발생했습니다.');
        }
    });
}

// document.querySelectorAll('.delete-button').forEach(button => {
//     button.addEventListener('click', () => {
//         const resumeId = button.dataset.resumeId;
//         const companyId = /* 서버에서 렌더링한 companyId 사용 or 전역 변수로 삽입 */ 1;
//
//         fetch(`/job/resume-bookmark/delete?companyId=${companyId}&resumeId=${resumeId}`, {
//             method: 'DELETE'
//         }).then(res => {
//             if (res.ok) {
//                 alert('삭제되었습니다.');
//                 location.reload();
//             } else {
//                 alert('삭제 실패');
//             }
//         });
//     });
// });

// 개별 즐겨찾기 삭제 요청
function deleteBookmark(button) {
    const resumeId = button.getAttribute('data-resume-id');

    fetch(`/resume-bookmarks/${resumeId}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            alert('즐겨찾기에서 제거되었습니다.');
            button.closest('tr').remove();
        } else {
            alert('즐겨찾기 해제 중 오류가 발생했습니다.');
        }
    });
}

// 전체 선택 토글
function toggleAll(masterCheckbox) {
    const checkboxes = document.querySelectorAll('input[name="resumeIds"]');
    checkboxes.forEach(cb => cb.checked = masterCheckbox.checked);
}