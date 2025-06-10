package com.multi.matchingbot.notification.controller;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.notification.domain.dto.NotificationDto;
import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 알림창
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
    
    // 알림 상세보기
    @GetMapping("/detail/{id}")
    public String showNotificationDetail(@PathVariable("id") Long id, Model model) {
        Notification notification = notificationService.markAsReadAndReturn(id);
        model.addAttribute("notification", notification);
        model.addAttribute("companyName", notification.getMember().getName());
        return "notification/notification-detail";
    }

    // 읽은 알림 전체 삭제
    @DeleteMapping("/delete-read-all")
    @ResponseBody
    public ResponseEntity<Void> deleteAllReadNotifications(@AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getId(); // 로그인 사용자 ID
        notificationService.deleteAllReadNotifications(memberId);
        return ResponseEntity.ok().build();
    }

    // 읽은 알림 페이징
    @GetMapping("/read-list")
    @ResponseBody
    public Page<NotificationDto> getReadNotifications(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal MBotUserDetails userDetails) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationService.getPagedReadNotifications(userDetails.getId(), pageable);
    }
    
    // 안읽은 알림 실시간
    @GetMapping("/has-unread")
    @ResponseBody
    public boolean hasUnread(@AuthenticationPrincipal MBotUserDetails userDetails) {
        if (userDetails == null || userDetails.getRole() != Role.MEMBER) return false;
        return notificationService.hasUnread(userDetails.getId());
    }

    // 안읽은 알림 전체 읽음 처리
    @PostMapping("/mark-all-as-read")
    @ResponseBody
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal MBotUserDetails userDetails) {
        if (userDetails == null || userDetails.getRole() != Role.MEMBER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok().build();
    }

}
