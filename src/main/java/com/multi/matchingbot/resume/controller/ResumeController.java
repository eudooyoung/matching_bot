package com.multi.matchingbot.resume.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.service.MemberResumeService;
import com.multi.matchingbot.notification.service.NotificationService;
import com.multi.matchingbot.resume.domain.dto.ResumeDetailDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeDetailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

    private final MemberResumeService memberResumeService;
    private final ResumeDetailMapper resumeDetailMapper;
    private final JobService jobService;
    private final NotificationService notificationService;

    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    @GetMapping("/detail/{id}")
    public String resumeDetail(@PathVariable("id") Long resumeId, Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Resume resume = memberResumeService.findByIdWithAll(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서 입니다."));

        log.warn("이력서 상세 컨트롤러 호출");

        /*매칭률 검색용 공고 목록*/
        List<Job> jobs = jobService.findByCompanyId(userDetails.getId());
        model.addAttribute("jobs", jobs);

        /*이력서 출력용 resumeDto*/
        ResumeDetailDto resumeDetailDto = resumeDetailMapper.toDto(resume);
        model.addAttribute("resume", resumeDetailDto);

        /*이력서 열람 알림용*/
        Long resumeOwnerId = resume.getMember().getId(); // 이력서 주인
        String companyName = userDetails.getCompanyName(); // 로그인한 기업 이름
        log.info("열람서비스 호출 직전");
        notificationService.sendResumeViewedNotification(resumeOwnerId, companyName, resume.getTitle(), resume.getId());
        return "resume/detail";
    }
}
