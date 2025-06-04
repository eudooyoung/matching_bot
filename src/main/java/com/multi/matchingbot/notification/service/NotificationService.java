package com.multi.matchingbot.notification.service;

import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByMemberIdAndStatus(memberId, NotificationStatus.UNREAD);
    }

    public List<Notification> getReadNotifications(Long memberId) {
        return notificationRepository.findByMemberIdAndStatus(memberId, NotificationStatus.READ);
    }
}
