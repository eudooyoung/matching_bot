package com.multi.matchingbot.notification.domain.entity;

import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

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
}

