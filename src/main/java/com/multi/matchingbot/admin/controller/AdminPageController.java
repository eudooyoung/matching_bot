package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.AdminPageService;
import com.multi.matchingbot.admin.domain.AdminPagedResult;
import com.multi.matchingbot.admin.domain.AdminSearchCondition;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.admin.domain.CompanyAdminView;
import com.multi.matchingbot.admin.domain.MemberAdminView;
import com.multi.matchingbot.admin.domain.ResumeAdminView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void members(@ModelAttribute AdminSearchCondition condition, Model model) {
        AdminPagedResult<MemberAdminView> result = adminPageService.members(condition);
        model.addAttribute("members", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
    }

    @GetMapping("/companies")
    public void companies(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getStatus());
        AdminPagedResult<CompanyAdminView> result = adminPageService.companies(condition);
        model.addAttribute("companies", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
    }

    @GetMapping("/resumes")
    public void resumes(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getKeywordsStatus());
        AdminPagedResult<ResumeAdminView> result = adminPageService.resumes(condition);
        model.addAttribute("resumes", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
    }



}
