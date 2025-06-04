package com.multi.matchingbot.notification.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String status; // "UNREAD" 또는 "READ"

}
