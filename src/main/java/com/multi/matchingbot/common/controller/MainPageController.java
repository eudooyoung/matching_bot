package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
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

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class MainPageController {

    private final JobService jobService;
    private final JobBookmarkService jobBookmarkService;

    @GetMapping({"/", "/main"})
    public String mainPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            Model model,
            @AuthenticationPrincipal MBotUserDetails user) {

        log.info("mainPage 호출됨. 사용자 정보: {}", user);

        if (user != null) {
            log.info("user.getRole() 값 직접 확인: '{}'", user.getRole());


            if (Role.COMPANY.equals(user.getRole())) {
                log.info("기업 회원이므로 /resumes 리다이렉트");
                return "redirect:/resumes";
            }

            model.addAttribute("role", user.getRole().name()); // 문자열 "MEMBER", "COMPANY", "ADMIN" 등으로 변환

//            model.addAttribute("role", user.getRole());
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

        // 로그인한 개인회원의 경우 북마크 상태 확인
        if (user != null && Role.MEMBER.equals(user.getRole())) {
            List<Long> bookmarkedJobIds = jobBookmarkService.getBookmarkedJobIds(user.getMemberId());
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
