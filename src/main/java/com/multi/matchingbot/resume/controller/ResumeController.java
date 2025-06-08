package com.multi.matchingbot.resume.controller;

import com.multi.matchingbot.member.service.MemberResumeService;
import com.multi.matchingbot.resume.domain.dto.ResumeDetailDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeDetailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

    private final MemberResumeService memberResumeService;
    private final ResumeDetailMapper resumeDetailMapper;

    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    @GetMapping("/detail/{id}")
    public String resumeDetail(@PathVariable("id") Long resumeId, Model model) {
        Resume resume = memberResumeService.findByIdWithAll(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서 입니다."));

        ResumeDetailDto resumeDetailDto = resumeDetailMapper.toDto(resume);
        model.addAttribute("resume", resumeDetailDto);
        return "resume/detail";
    }
}
