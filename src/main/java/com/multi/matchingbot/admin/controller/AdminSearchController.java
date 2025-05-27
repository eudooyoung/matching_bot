package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.AdminSearchService;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.member.domain.dtos.MemberAdminViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminSearchController {

    private final AdminSearchService adminSearchService;

    @GetMapping("/members/search")
    public Page<MemberAdminViewDto> memberSearch(@ModelAttribute SearchCondition cond) {
        return adminSearchService.memberSearch(cond, cond.toPageable());
    }
}
