package com.multi.matchingbot.notification.service;

import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByStatus(Long memberId, NotificationStatus status) {
        return notificationRepository.findByMemberIdAndStatus(memberId, status);
    }

    @Transactional
    public Notification markAsReadAndReturn(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));

        if (notification.getStatus() == NotificationStatus.UNREAD) {
            notification.setStatus(NotificationStatus.READ);
        }
        return notification;
    }
}
