package com.multi.matchingbot.notification.service;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.repository.JobRepository;
import com.multi.matchingbot.member.domain.entities.CompanyBookmark;
import com.multi.matchingbot.member.repository.CompanyBookmarkRepository;
import com.multi.matchingbot.notification.domain.entity.Notification;
import com.multi.matchingbot.notification.domain.enums.NotificationStatus;
import com.multi.matchingbot.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public boolean hasUnread(Long memberId) {
        return notificationRepository.existsByMemberIdAndStatus(memberId, NotificationStatus.UNREAD);
    }
}
