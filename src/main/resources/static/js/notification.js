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

        // 처음 진입하거나 삭제 후 초기화된 경우 로드
        const readContainer = document.getElementById('readNotificationList');
        if (readContainer.children.length === 0) {
            loadReadNotifications();
        }

        // 스크롤 이벤트 등록
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

                // DOM 초기화 및 무한 스크롤 초기화
                document.getElementById('readNotificationList').innerHTML = '';
                readPage = 0;
                readLastPage = false;

                loadReadNotifications(); // 다시 첫 페이지 로딩
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

// 무한 스크롤 페이징 상태 변수
let readPage = 0;
let readLoading = false;
let readLastPage = false;

// 읽은 탭 스크롤 감지 핸들러
function readScrollHandler() {
    if (readLoading || readLastPage) return;

    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const documentHeight = document.body.scrollHeight;

    if (scrollTop + windowHeight >= documentHeight - 100) {
        loadReadNotifications();
    }
}

// 읽은 알림 로딩 함수
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

            // 👇 추가된 메시지 표시
            if (readLastPage) {
                document.getElementById('readEndMessage').style.display = 'block';
            }
        })
}

// 페이지 첫 로드시 기본 탭 실행
document.addEventListener('DOMContentLoaded', () => {
    showTab('unread'); // 기본으로 '읽지 않음' 탭 활성화
});