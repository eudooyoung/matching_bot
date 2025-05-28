package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/bulk")
@Controller
public class AdminBulkController {

    private final MemberService memberService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("members")
    public String bulkActionMember(@RequestParam(name = "checkedIds") List<Long> checkedIds,
                                   @RequestParam(name = "actionType") String actionType) {
        if ("DELETE".equals(actionType)) {
            memberService.deactivateBulks(checkedIds);
        } else if ("RESTORE".equals(actionType)) {
            memberService.reactivateBulks(checkedIds);
        }
        return "redirect:/admin/members";
    }
}
