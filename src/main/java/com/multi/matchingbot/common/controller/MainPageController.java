package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.service.MemberResumeService;
import com.multi.matchingbot.notification.service.NotificationService;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeDetailMapper;
import com.multi.matchingbot.resume.service.ResumeService;
import com.multi.matchingbot.member.service.JobBookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class MainPageController {

    private final MemberResumeService memberResumeService;
    private final ResumeDetailMapper resumeDetailMapper;
    private final JobService jobService;
    private final JobBookmarkService jobBookmarkService;
    private final NotificationService notificationService;
    private final ResumeService resumeService;

    @GetMapping({"/", "/main"})
    public String mainPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            Model model,
            @AuthenticationPrincipal MBotUserDetails userDetails) {

        String role = null;
        List<Resume> resumes = Collections.emptyList(); // 기본값

        log.info("mainPage 호출됨. 사용자 정보: {}", userDetails);

        if (userDetails != null) {
            log.info("user.getRole() 값 직접 확인: '{}'", userDetails.getRole());


            if ("COMPANY".equals(userDetails.getRole())) {
                log.info("기업 회원이므로 /resumes 리다이렉트");
                return "redirect:/resumes";
            }

            model.addAttribute("role", userDetails.getRole().name()); // 문자열 "MEMBER", "COMPANY", "ADMIN" 등으로 변환

//            model.addAttribute("role", userDetails.getRole());
        } else {
            log.info("비회원 접근");
            model.addAttribute("role", null);
        }

        // 👉 개인회원 or 비회원에게만 채용공고 보이게
        int pageIndex = Math.max(0, page - 1);
        Page<Job> jobPage = jobService.getPageJobs(PageRequest.of(pageIndex, size));

        model.addAttribute("jobList", jobPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());

        if (userDetails != null) {
            role = userDetails.getRole().name();

            resumes = resumeService.findByMemberId(userDetails.getMemberId());
            model.addAttribute("resumes", resumes);
        }

        model.addAttribute("role", role);

        // 로그인한 개인회원의 경우 북마크 상태 확인
        if (userDetails != null && Role.MEMBER.equals(userDetails.getRole())) {
            List<Long> bookmarkedJobIds = jobBookmarkService.getBookmarkedJobIds(userDetails.getMemberId());
            log.info("북마크된 채용공고 IDs: {}", bookmarkedJobIds);
            model.addAttribute("bookmarkedJobIds", bookmarkedJobIds);
        } else {
            // 구직자가 아닌 경우 기본값 설정 (안전장치)
            log.info("비회원 또는 기업회원이므로 빈 북마크 리스트 설정");
            model.addAttribute("bookmarkedJobIds", java.util.Collections.emptyList());
        }

        return "main/main";
    }

    @GetMapping("/calendar")
    public String calendarPage() {
        return "main/calendar";
    }
}
