package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.service.ResumeService;  // ✅ member.service 로 수정!
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMPANY')")
public class CompanyResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public String resumeList(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "6") int size,
                             Model model) {

        log.info("📄 resumeList() 컨트롤러 도달!"); // 로그 추가

        int pageIndex = Math.max(0, page - 1);
        Page<ResumeDto> resumePage = resumeService.getPageResumes(PageRequest.of(pageIndex, size));

        model.addAttribute("resumeList", resumePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resumePage.getTotalPages());

        return "resume/list";
    }
}
