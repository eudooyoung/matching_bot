package com.multi.matchingbot.notification.repository;

import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberIdAndStatus(Long memberId, NotificationStatus status);
    List<Notification> findByMemberId(Long memberId);
}