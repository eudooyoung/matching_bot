package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.ContentStatsDto;
import com.multi.matchingbot.admin.repository.*;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AdminMainService {
    private final MemberAdminRepository memberAdminRepository;
    private final CompanyAdminRepository companyAdminRepository;
    private final ResumeAdminRepository resumeAdminRepository;
    private final JobAdminRepository jobAdminRepository;
    private final CommunityAdminRepository communityAdminRepository;

    public ContentStatsDto getContentStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        LocalDateTime weekStart = now.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay();
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime monthStart = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = thisMonth.plusMonths(1).atDay(1).atStartOfDay();

        return ContentStatsDto.builder()
                .memberToday(memberAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .memberWeek(memberAdminRepository.countByCreatedAtBetween(weekStart, now))
                .memberMonth(memberAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .memberTotal((int)memberAdminRepository.countByStatusAndRole(Yn.Y, Role.MEMBER))

                .companyToday(companyAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .companyWeek(companyAdminRepository.countByCreatedAtBetween(weekStart, now))
                .companyMonth(companyAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .companyTotal((int)companyAdminRepository.countByStatus(Yn.Y))

                .resumeToday(resumeAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .resumeWeek(resumeAdminRepository.countByCreatedAtBetween(weekStart, now))
                .resumeMonth(resumeAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .resumeTotal((int)resumeAdminRepository.count())

                .jobToday(jobAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .jobWeek(jobAdminRepository.countByCreatedAtBetween(weekStart, now))
                .jobMonth(jobAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .jobTotal((int)jobAdminRepository.count())

                .communityPostToday(communityAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .communityPostWeek(communityAdminRepository.countByCreatedAtBetween(weekStart, now))
                .communityPostMonth(communityAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .communityPostTotal((int)communityAdminRepository.count())

                .build();
    }
}
