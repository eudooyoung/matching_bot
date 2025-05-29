package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class ResumeController {

    private final ResumeService resumeService;

    // 목록
    @GetMapping
    public String list(Model model) {
        List<Resume> resumes = resumeService.findAll();
        model.addAttribute("resumes", resumes);
        return "member/member-resume-list";
    }

    // 상세 보기
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "member/view"; // 향후 추가
    }

    // 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "member/edit"; // 향후 추가
    }

    // 단일 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        resumeService.delete(id);
        return "redirect:/member";
    }

    // 선택 삭제
    @PostMapping("/delete-bulk")
    public String deleteBulk(@RequestParam(required = false, name = "checkedIds") List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            resumeService.deleteAllByIds(ids);
        }
        return "redirect:/member";
    }
}