package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bulk")
@RestController
public class AdminBulkController {

    private final MemberService memberService;
    private final CompanyService companyService;
    private final ResumeService resumeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("members")
    public String bulkActionMember(@RequestParam(name = "checkedIds") List<Long> checkedIds,
                                   @RequestParam(name = "actionType") String actionType) {
        if ("DELETE".equals(actionType)) {
            memberService.deactivateBulk(checkedIds);
        } else if ("RESTORE".equals(actionType)) {
            memberService.reactivateBulk(checkedIds);
        }
        return "redirect:/admin/members";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("companies")
    public String bulkActionCompany(@RequestParam(name = "checkedIds") List<Long> checkedIds,
                                    @RequestParam(name = "actionType") String actionType) {
        if ("DELETE".equals(actionType)) {
            companyService.deactivateBulk(checkedIds);
        } else if ("RESTORE".equals(actionType)) {
            companyService.reactivateBulk(checkedIds);
        }
        return "redirect:/admin/companies";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("resumes")
    public String bulkDeleteResumeHard(@RequestParam(name = "checkedIds") List<Long> checkedIds) {
        resumeService.deleteBulkHard(checkedIds);
        return "redirect:/admin/resumes";
    }
}
