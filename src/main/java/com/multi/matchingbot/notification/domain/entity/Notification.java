package com.multi.matchingbot.notification.domain.entity;

import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.domain.enums.NotificationTargetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private NotificationTargetType targetType;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}