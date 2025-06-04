package com.multi.matchingbot.notification.repository;

import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberIdAndStatusOrderByCreatedAtDesc(Long memberId, NotificationStatus status);

    // 테스트 용도
//    void deleteByStatusAndCreatedAtBefore(NotificationStatus status, LocalDateTime time);

    void deleteByStatusAndCreatedAtBefore(NotificationStatus status, LocalDateTime beforeTime);

    boolean existsByMemberIdAndStatus(Long memberId, NotificationStatus notificationStatus);
}