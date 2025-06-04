package com.multi.matchingbot.notification.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

//    @GetMapping("/notifications")
//    public String showNotifications(@RequestParam(name = "tab", defaultValue = "unread") String tab,
//                                    @AuthenticationPrincipal MBotUserDetails userDetails,
//                                    Model model) {
//        Long memberId = userDetails.getId(); // 여기서 로그인한 사용자의 ID 얻기
//
//        NotificationStatus status = tab.equals("read") ? NotificationStatus.READ : NotificationStatus.UNREAD;
//        List<Notification> notifications = notificationService.getNotificationsByStatus(memberId, status);
//
//        model.addAttribute("notifications", notifications);
//        model.addAttribute("tab", tab);
//
//        System.out.println("현재 로그인한 사용자 ID: " + memberId);
//        System.out.println("탭 상태: " + tab);
//        System.out.println("알림 수: " + notifications.size());
//
//        return "notification/notification";
//    }

    @GetMapping("/notifications")
    public String showAllNotifications(@AuthenticationPrincipal MBotUserDetails userDetails,
                                       Model model) {
        Long memberId = userDetails.getId();

        List<Notification> unreadNotifications = notificationService.getNotificationsByStatus(memberId, NotificationStatus.UNREAD);
        List<Notification> readNotifications = notificationService.getNotificationsByStatus(memberId, NotificationStatus.READ);

        model.addAttribute("unreadNotifications", unreadNotifications);
        model.addAttribute("readNotifications", readNotifications);

        return "notification/notification"; // 이 HTML 경로
    }

    @GetMapping("/detail/{id}")
    public String showNotificationDetail(@PathVariable("id") Long id, Model model) {
        Notification notification = notificationService.markAsReadAndReturn(id);
        model.addAttribute("notification", notification);
        model.addAttribute("companyName", notification.getMember().getName());
        return "notification/notification-detail";
    }
}
