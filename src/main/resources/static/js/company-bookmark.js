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

    fetch('/api/member/company-bookmark/delete', {
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
        })
        .catch(error => {
            console.error('Error:', error);
            alert("삭제 중 오류가 발생했습니다.");
        });
}

// 북마크 토글 (★ 클릭)
function toggleBookmark(button) {
    const companyId = button.getAttribute('data-company-id');
    const isCurrentlyBookmarked = button.classList.contains('bookmarked');

    fetch(`/api/member/company-bookmark/toggle/${companyId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('북마크 처리에 실패했습니다.');
            }
        })
        .then(message => {
            if (isCurrentlyBookmarked) {
                // 북마크 해제됨 - 페이지에서 해당 행 제거
                const row = button.closest('tr');
                row.style.transition = 'opacity 0.3s ease';
                row.style.opacity = '0';
                setTimeout(() => {
                    row.remove();
                    // 테이블이 비어있으면 메시지 표시
                    checkEmptyTable();
                }, 300);
            } else {
                // 북마크 추가됨 (이 페이지에서는 발생하지 않음)
                button.classList.remove('not-bookmarked');
                button.classList.add('bookmarked');
                button.title = '북마크 해제';
            }

            // 성공 메시지 표시
            showMessage(message, 'success');
        })
        .catch(error => {
            console.error('Error:', error);
            alert("북마크 처리 중 오류가 발생했습니다.");
        });
}

// 기업 상세보기
function showCompanyDetail(companyId) {
    // 기업 상세 페이지로 이동 (실제 URL은 프로젝트에 맞게 수정)
    window.open(`/company/detail/${companyId}`, '_blank');
}

// 빈 테이블 확인
function checkEmptyTable() {
    const tbody = document.querySelector('.company-table tbody');
    const rows = tbody.querySelectorAll('tr');

    if (rows.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 40px; color: #666;">
                    관심 기업이 없습니다.
                </td>
            </tr>
        `;

        // 삭제 버튼 비활성화
        const deleteButton = document.querySelector('.delete-button');
        if (deleteButton) {
            deleteButton.disabled = true;
            deleteButton.style.opacity = '0.5';
        }
    }
}

// 메시지 표시 함수
function showMessage(message, type = 'info') {
    // 기존 메시지 제거
    const existingMessage = document.querySelector('.message-popup');
    if (existingMessage) {
        existingMessage.remove();
    }

    // 새 메시지 생성
    const messageDiv = document.createElement('div');
    messageDiv.className = `message-popup ${type}`;
    messageDiv.textContent = message;
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 4px;
        color: white;
        font-weight: bold;
        z-index: 1000;
        opacity: 0;
        transition: opacity 0.3s ease;
        ${type === 'success' ? 'background-color: #28a745;' : 'background-color: #dc3545;'}
    `;

    document.body.appendChild(messageDiv);

    // 애니메이션 효과
    setTimeout(() => {
        messageDiv.style.opacity = '1';
    }, 100);

    // 3초 후 자동 제거
    setTimeout(() => {
        messageDiv.style.opacity = '0';
        setTimeout(() => {
            if (messageDiv.parentNode) {
                messageDiv.parentNode.removeChild(messageDiv);
            }
        }, 300);
    }, 3000);
}

console.log()

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 빈 테이블 확인
    checkEmptyTable();
});