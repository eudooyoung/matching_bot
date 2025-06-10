function pollUnreadNotifications() {
    fetch('/notification/has-unread?_=' + Date.now())
        .then(res => res.json())
        .then(hasUnread => {
            const bell = document.querySelector('.notification-bell');
            if (!bell) return;

            let dot = bell.querySelector('.notification-dot');

            // 점이 없으면 생성
            if (!dot) {
                dot = document.createElement('span');
                dot.className = 'notification-dot';
                bell.appendChild(dot);
            }

            // 점 표시 여부 갱신
            dot.style.display = hasUnread ? 'inline-block' : 'none';
        })
        .catch(err => {
            console.error('🔴 알림 polling 실패:', err);
        });
}

// 초기 polling 1회
document.addEventListener('DOMContentLoaded', () => {
    pollUnreadNotifications();
});

// 주기적으로 polling (5초마다)
setInterval(pollUnreadNotifications, 5000);