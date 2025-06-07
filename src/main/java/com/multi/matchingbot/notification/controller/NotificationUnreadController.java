package com.multi.matchingbot.notification.controller;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.notification.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificationUnreadController {

    private final NotificationService notificationService;

    public NotificationUnreadController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute("hasUnreadNotification")
    public boolean hasUnreadNotification(@AuthenticationPrincipal MBotUserDetails userDetails) {
        if (userDetails == null) return false;
        if (userDetails.getRole() != Role.MEMBER) return false; // 개인회원만 알림 표시

        return notificationService.hasUnread(userDetails.getId());
    }
}
