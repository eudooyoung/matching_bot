package com.multi.matchingbot.notification.service;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.repository.JobRepository;
import com.multi.matchingbot.member.domain.entity.CompanyBookmark;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.CompanyBookmarkRepository;
import com.multi.matchingbot.notification.domain.dto.NotificationDto;
import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CompanyBookmarkRepository companyBookmarkRepository;
    private final JobRepository jobRepository;

    public NotificationService(NotificationRepository notificationRepository, CompanyBookmarkRepository companyBookmarkRepository, JobRepository jobRepository) {
        this.notificationRepository = notificationRepository;
        this.companyBookmarkRepository = companyBookmarkRepository;
        this.jobRepository = jobRepository;
    }

    public List<Notification> getNotificationsByStatus(Long memberId, NotificationStatus status) {
        return notificationRepository.findByMemberIdAndStatusOrderByCreatedAtDesc(memberId, status);
    }

    @Transactional
    public Notification markAsReadAndReturn(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));

        if (notification.getStatus() == NotificationStatus.UNREAD) {
            notification.setStatus(NotificationStatus.READ);
        }
        return notification;
    }

    // 채용 공고 등록 알림
    public void sendJobNotificationToBookmarkedMembers(Long companyId, String companyName, String jobTitle) {
        List<CompanyBookmark> bookmarks = companyBookmarkRepository.findByCompanyId(companyId);

        for (CompanyBookmark bookmark : bookmarks) {
            Notification notification = Notification.builder()
                    .member(bookmark.getMember())
                    .title("관심 기업 채용공고 등록")
                    .content("[" + companyName + "]에서 '" + jobTitle + "' 채용공고가 등록되었습니다.")
                    .status(NotificationStatus.UNREAD)
                    .build();
            notificationRepository.save(notification);
        }
    }

    // 채용 마감 알림
    @Transactional
    public void sendDeadlineApproachingNotifications() {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(3);

        List<Job> expiringJobs = jobRepository.findByEndDateBetween(now, threshold);

        for (Job job : expiringJobs) {
            List<CompanyBookmark> bookmarks = companyBookmarkRepository.findByCompanyId(job.getCompany().getId());

            for (CompanyBookmark bookmark : bookmarks) {
                Notification notification = Notification.builder()
                        .member(bookmark.getMember())
                        .title("관심 기업의 채용 마감이 임박했습니다")
                        .content(job.getTitle() + " 채용이 곧 마감됩니다. 지금 확인해보세요!")
                        .status(NotificationStatus.UNREAD)
                        .createdAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);
            }
        }
    }

    // 테스트 용도
//    @Transactional
//    public void deleteReadNotificationsOlderThan(Duration duration) {
//        LocalDateTime threshold = LocalDateTime.now().minus(duration);
//        notificationRepository.deleteByStatusAndCreatedAtBefore(NotificationStatus.READ, threshold);
//    }

    // 시간 지나면 알림 삭제
    @Transactional
    public void deleteOldReadNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30); // 30일
        notificationRepository.deleteByStatusAndCreatedAtBefore(NotificationStatus.READ, threshold);
    }

    // 안읽은 알림
    public boolean hasUnread(Long memberId) {
        return notificationRepository.existsByMemberIdAndStatus(memberId, NotificationStatus.UNREAD);
    }

    // 읽은 알림 전체 삭제
    @Transactional
    public void deleteAllReadNotifications(Long memberId) {
        notificationRepository.deleteAllByMemberIdAndStatus(memberId, NotificationStatus.READ);
    }

    // 읽은 알림 페이징
    public Page<NotificationDto> getPagedReadNotifications(Long memberId, Pageable pageable) {
        return notificationRepository.findByMemberIdAndStatus(memberId, NotificationStatus.READ, pageable)
                .map(entity -> NotificationDto.builder()
                        .id(entity.getId())
                        .memberId(entity.getMember().getId())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .status(entity.getStatus())
                        .build());
    }

    // 이력서 열람 알림
    @Transactional
    public void sendResumeViewedNotification(Long memberId, String companyName, String resumeTitle) {
        Notification notification = Notification.builder()
                .member(Member.builder().id(memberId).build())
                .title("이력서 열람 알림")
                .content("[" + companyName + "]에서 '" + resumeTitle + "' 이력서를 열람했습니다.")
                .status(NotificationStatus.UNREAD)
                .build();
        notificationRepository.save(notification);
    }

    // 전체 읽음 처리
    @Transactional
    public void markAllAsRead(Long memberId) {
        List<Notification> unreadNotifications =
                notificationRepository.findByMemberIdAndStatus(memberId, NotificationStatus.UNREAD);

        for (Notification notification : unreadNotifications) {
            notification.setStatus(NotificationStatus.READ);
        }
    }
}
