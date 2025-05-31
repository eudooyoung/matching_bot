package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.*;
import com.multi.matchingbot.admin.service.AdminBoardService;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/board")
public class AdminBoardController {

    private final AdminBoardService adminPageService;
    private final JobService jobService;
    private final CompanyService companyService;
    private final MemberService memberService;
    private final ResumeService resumeService;

    @GetMapping("/members")
    public String members(@ModelAttribute AdminSearchCondition condition, Model model) {
        AdminPagedResult<MemberAdminView> result = adminPageService.members(condition);
        model.addAttribute("members", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
        return "/admin/board-members";
    }

    //    구직자 회원 정보 임시 매핑
    @GetMapping("/members/{memberId}")
    public String adminMemberDetail(@PathVariable(name = "memberId") Long memberId, Model model) {
        model.addAttribute("memberId", memberService.getMemberById(memberId));
        return "members/profile";
    }

    @GetMapping("/companies")
    public String companies(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getStatus());
        AdminPagedResult<CompanyAdminView> result = adminPageService.companies(condition);
        model.addAttribute("companies", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
        return "/admin/board-companies";
    }

    @GetMapping("/companies/{companyId}")
    public String adminCompanyHome(@PathVariable(name = "companyId") Long companyId, Model model) {
        model.addAttribute("companyId", companyId);
        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);
        return "company/index";
    }

    @GetMapping("companies/edit-profile/{companyId}")
    public String adminCompanyDetail(@PathVariable(name = "companyId") Long companyId, Model model) {
        model.addAttribute("company", companyService.findById(companyId));
        return "company/edit-profile";
    }

    @GetMapping("/resumes")
    public String resumes(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getKeywordsStatus());
        AdminPagedResult<ResumeAdminView> result = adminPageService.resumes(condition);
        model.addAttribute("resumes", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
        return "/admin/board-resumes";
    }

    @GetMapping("/resumes/{resumeId}")
    public String adminResumeDetail(@PathVariable(name = "resumeId") Long resumeId, Model model) {
        Resume resume = resumeService.findById(resumeId);
        model.addAttribute("resume", resume);
        return "member/member-edit";
    }
}
