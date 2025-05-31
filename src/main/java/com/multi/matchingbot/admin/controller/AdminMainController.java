package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.AdminMainService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMainController {

    private final AdminMainService adminMainService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/", "/main", ""})
    public String mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if (user != null) {
            log.info("userType: {}", user.getRole());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("role", null);
            log.info("비회원 접근");
        }

        // 전체 수
        model.addAttribute("memberCount", adminMainService.countActiveMembers());
        model.addAttribute("companyCount", adminMainService.countActiveCompanies());
        model.addAttribute("resumeCount", adminMainService.countResumes());
        model.addAttribute("jobCount", adminMainService.countJobs());
        model.addAttribute("communityPostCount", adminMainService.countCommunityPosts());

        // 테이블별 현황
        model.addAttribute("contentStats", adminMainService.getContentStats());

        // 그래프 데이터
        model.addAttribute("userTypeLabels", List.of("개인회원", "기업회원"));
        model.addAttribute("userTypeData", List.of(
                adminMainService.countActiveMembers(),
                adminMainService.countActiveCompanies()
        ));

        model.addAttribute("resumeJobLabels", List.of("이력서", "채용공고"));
        model.addAttribute("resumeJobData", List.of(
                adminMainService.countResumes(),
                adminMainService.countJobs()
        ));

        return "admin/main";
    }

    @GetMapping("/login")
    public void login() {
    }
}
