package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.AdminPageService;
import com.multi.matchingbot.common.domain.dto.PagedResult;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.member.domain.dtos.MemberAdminViewDto;
import com.multi.matchingbot.member.domain.dtos.ResumeAdminViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminPageService adminPageService;

    @GetMapping({"/", "/main", ""})
    public String mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if (user != null) {
            log.info("userType: {}", user.getRole());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("role", null);
            log.info("비회원 접근");
        }
        return "main/main";
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/members")
    public void members(@ModelAttribute SearchCondition cond, Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        PagedResult<MemberAdminViewDto> result = adminPageService.members(cond);
        model.addAttribute("members", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("cond", cond);
    }



    @GetMapping("/resumes")
    public void resumes(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        PagedResult<ResumeAdminViewDto> result = adminPageService.resumes(page);
        model.addAttribute("resumes", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
    }


}
