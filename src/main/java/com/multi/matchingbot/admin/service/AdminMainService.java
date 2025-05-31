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

    public long countActiveMembers() {
        return memberAdminRepository.countByStatusAndRole(Yn.Y, Role.MEMBER);
    }

    public long countActiveCompanies() {
        return companyAdminRepository.countByStatus(Yn.Y);
    }

    public long countResumes() {
        return resumeAdminRepository.count();
    }

    public long countJobs() {
        return jobAdminRepository.count();
    }

    public long countCommunityPosts() {
        return communityAdminRepository.count();
    }

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

                .companyToday(companyAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .companyWeek(companyAdminRepository.countByCreatedAtBetween(weekStart, now))
                .companyMonth(companyAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))

                .resumeToday(resumeAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .resumeWeek(resumeAdminRepository.countByCreatedAtBetween(weekStart, now))
                .resumeMonth(resumeAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))

                .jobToday(jobAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .jobWeek(jobAdminRepository.countByCreatedAtBetween(weekStart, now))
                .jobMonth(jobAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))

                .communityPostToday(communityAdminRepository.countByCreatedAtBetween(todayStart, todayEnd))
                .communityPostWeek(communityAdminRepository.countByCreatedAtBetween(weekStart, now))
                .communityPostMonth(communityAdminRepository.countByCreatedAtBetween(monthStart, monthEnd))
                .build();
    }
}
