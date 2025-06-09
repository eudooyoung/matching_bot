function pollUnreadNotifications() {
    fetch('/notification/has-unread')
        .then(res => res.json())
        .then(hasUnread => {
            const dot = document.querySelector('.notification-dot');
            const bell = document.querySelector('.notification-bell');

            if (dot) {
                dot.style.display = hasUnread ? 'inline-block' : 'none';
            } else if (hasUnread && bell) {
                // 알림 점이 없다면 새로 생성
                const newDot = document.createElement('span');
                newDot.className = 'notification-dot';
                newDot.style.display = 'inline-block';
                bell.parentNode.appendChild(newDot);
            }
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