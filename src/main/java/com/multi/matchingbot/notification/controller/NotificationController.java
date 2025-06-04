package com.multi.matchingbot.notification.controller;

import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.notification.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    private final MemberService memberService;
    private final NotificationService notificationService;

    public NotificationController(MemberService memberService, NotificationService notificationService) {
        this.memberService = memberService;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String showUnread(Model model, Principal principal) {
        Long memberId = memberService.findIdByPrincipal(principal);

        model.addAttribute("unreadNotifications", notificationService.getUnreadNotifications(memberId));
        model.addAttribute("readNotifications", notificationService.getReadNotifications(memberId));

        return "notification/notification";
    }
}
