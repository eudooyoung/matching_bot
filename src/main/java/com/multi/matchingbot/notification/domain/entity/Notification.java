package com.multi.matchingbot.notification.domain.entity;

import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification")
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private NotificationStatus status;
}

