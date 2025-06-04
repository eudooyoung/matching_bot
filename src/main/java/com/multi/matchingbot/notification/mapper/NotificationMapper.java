package com.multi.matchingbot.notification.mapper;

import com.multi.matchingbot.notification.domain.dto.NotificationDto;
import com.multi.matchingbot.notification.domain.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public interface NotificationMapper {

    static NotificationDto toDto(Notification entity) {
        return NotificationDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .status(entity.getStatus())
                .build();
    }
}
