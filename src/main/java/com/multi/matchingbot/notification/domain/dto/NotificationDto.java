package com.multi.matchingbot.notification.domain.dto;

import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
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
    private String companyName;
    private NotificationStatus status;

}
