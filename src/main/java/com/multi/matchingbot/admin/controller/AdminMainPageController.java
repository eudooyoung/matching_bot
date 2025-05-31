package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.ContentStatsDto;
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
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMainPageController {

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

        //스탯dto
        ContentStatsDto statsDto = adminMainService.getContentStats();

        // 테이블별 현황
        model.addAttribute("contentStats", statsDto);

        // 그래프 데이터
        Map<String, List<Integer>> userTypeDataByPeriod = Map.of(
                "today", List.of(statsDto.getMemberToday(), statsDto.getCompanyToday()),
                "week", List.of(statsDto.getMemberWeek(), statsDto.getCompanyWeek()),
                "month", List.of(statsDto.getMemberMonth(), statsDto.getCompanyMonth()),
                "total", List.of(statsDto.getMemberTotal(), statsDto.getCompanyTotal())
        );
        model.addAttribute("userTypeLabels", List.of("개인회원", "기업회원"));
        model.addAttribute("userTypeDataByPeriod", userTypeDataByPeriod);

        Map<String, List<Integer>> resumeJobDataByPeriod = Map.of(
                "today", List.of(statsDto.getResumeToday(), statsDto.getJobToday()),
                "week", List.of(statsDto.getResumeWeek(), statsDto.getJobWeek()),
                "month", List.of(statsDto.getResumeMonth(), statsDto.getJobMonth()),
                "total", List.of(statsDto.getResumeTotal(), statsDto.getJobTotal())
        );
        model.addAttribute("resumeJobLabels", List.of("이력서", "채용공고"));
        model.addAttribute("resumeJobDataByPeriod", resumeJobDataByPeriod);

        return "admin/main";
    }

    @GetMapping("/login")
    public void login() {
    }
}
