package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.CompanyAdminService;
import com.multi.matchingbot.admin.service.MemberAdminService;
import com.multi.matchingbot.admin.service.ResumeAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bulk")
@RestController
public class AdminBulkRestController {

    private final MemberAdminService memberAdminService;
    private final CompanyAdminService companyAdminService;
    private final ResumeAdminService resumeAdminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("members")
    public String bulkActionMember(@RequestParam(name = "checkedIds") List<Long> checkedIds,
                                   @RequestParam(name = "actionType") String actionType) {
        if ("DELETE".equals(actionType)) {
            memberAdminService.deactivateBulk(checkedIds);
        } else if ("RESTORE".equals(actionType)) {
            memberAdminService.reactivateBulk(checkedIds);
        }
        return "redirect:/admin/members";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("companies")
    public String bulkActionCompany(@RequestParam(name = "checkedIds") List<Long> checkedIds,
                                    @RequestParam(name = "actionType") String actionType) {
        if ("DELETE".equals(actionType)) {
            companyAdminService.deactivateBulk(checkedIds);
        } else if ("RESTORE".equals(actionType)) {
            companyAdminService.reactivateBulk(checkedIds);
        }
        return "redirect:/admin/companies";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("resumes")
    public String bulkDeleteResumeHard(@RequestParam(name = "checkedIds") List<Long> checkedIds) {
        resumeAdminService.deleteBulkHard(checkedIds);
        return "redirect:/admin/resumes";
    }
}
