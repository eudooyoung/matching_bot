package com.multi.matchingbot.notification;

import com.multi.matchingbot.notification.service.NotificationService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class NotificationScheduler {

    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 매일 새벽 4시: 마감 임박 알림 전송
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendDeadlineAlert() {
        notificationService.sendDeadlineApproachingNotifications();
    }

    // 매일 새벽 3시에 읽은 알림 삭제 실행
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanOldReadNotifications() {
        notificationService.deleteOldReadNotifications();
    }
}
