// 읽은/안읽은 알림 리스트 전환 및 무한스크롤 초기화
function showTab(tab) {
    const unreadTab = document.getElementById('unreadTab');
    const readTab = document.getElementById('readTab');
    const unreadList = document.getElementById('unreadList');
    const readList = document.getElementById('readList');

    if (tab === 'unread') {
        unreadList.style.display = 'block';
        readList.style.display = 'none';
        unreadTab.classList.add('active');
        readTab.classList.remove('active');

        // 읽은 탭 스크롤 이벤트 제거
        window.removeEventListener('scroll', readScrollHandler);
    } else {
        unreadList.style.display = 'none';
        readList.style.display = 'block';
        readTab.classList.add('active');
        unreadTab.classList.remove('active');

        const readContainer = document.getElementById('readNotificationList');
        if (readContainer.children.length === 0) {
            loadReadNotifications();
        }

        window.addEventListener('scroll', readScrollHandler);
    }
}

// 읽은 알림 전체 삭제
function deleteAllReadNotifications() {
    if (!confirm('읽은 알림을 모두 삭제하시겠습니까?')) {
        return;
    }

    fetch('/notification/delete-read-all', {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert('읽은 알림이 모두 삭제되었습니다.');
                document.getElementById('readNotificationList').innerHTML = '';
                readPage = 0;
                readLastPage = false;
                loadReadNotifications();
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

// 무한 스크롤 상태 변수
let readPage = 0;
let readLoading = false;
let readLastPage = false;

function readScrollHandler() {
    if (readLoading || readLastPage) return;

    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const documentHeight = document.body.scrollHeight;

    if (scrollTop + windowHeight >= documentHeight - 100) {
        loadReadNotifications();
    }
}

function loadReadNotifications() {
    readLoading = true;
    document.getElementById('readLoading').style.display = 'block';

    fetch(`/notification/read-list?page=${readPage}&size=10`)
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('readNotificationList');

            data.content.forEach(noti => {
                const div = document.createElement('div');
                div.className = 'notification-item read';
                div.textContent = noti.content;
                div.onclick = () => location.href = `/notification/detail/${noti.id}`;
                container.appendChild(div);
            });

            readPage++;
            readLastPage = data.last;
            readLoading = false;
            document.getElementById('readLoading').style.display = 'none';

            if (readLastPage) {
                document.getElementById('readEndMessage').style.display = 'block';
            }
        });
}

document.getElementById('markAllReadBtn').addEventListener('click', function () {
    fetch('/notification/mark-all-as-read', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(res => {
            if (res.ok) {
                alert("모든 알림을 읽음 처리했습니다.");
                location.reload(); // 또는 읽은/읽지 않은 목록을 JS로 분리 렌더링
            } else {
                alert("처리에 실패했습니다.");
            }
        })
        .catch(err => {
            console.error('알림 전체 읽음 처리 실패:', err);
        });
});

// 최초 페이지 진입 시 polling도 실행
document.addEventListener('DOMContentLoaded', () => {
    showTab('unread');
});