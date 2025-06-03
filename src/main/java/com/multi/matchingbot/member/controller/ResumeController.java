package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeAdminService resumeAdminService;
    private final MemberService memberService;

    // 목록
    @GetMapping
    public String list(Model model) {
        // 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 사용자 엔티티 조회
        Member member = memberService.findByUsername(email);
        Long memberId = member.getId();

        // 해당 사용자의 이력서만 조회
        List<Resume> resumes = resumeService.findByMemberId(memberId);
        model.addAttribute("resumes", resumes);
        return "member/member-resume-list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Resume resume = resumeService.findByIdWithOccupation(id);
        model.addAttribute("resume", resume);
        return "/member/resume-view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "/member/resume-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute Resume resume) {
        resumeService.updateResume(id, resume);
        return "redirect:/member/view/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        resumeAdminService.deleteHard(id);
        return "redirect:/member";
    }

    @PostMapping("/delete-bulk")
    public String deleteBulk(@RequestParam(required = false, name = "checkedIds") List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            resumeService.deleteAllByIds(ids);
        }
        return "redirect:/member";
    }

    @GetMapping("/insert")
    public String insertForm(Model model) {
        model.addAttribute("resume", new Resume()); // 혹은 ResumeDto 사용
        return "member/resume-insert";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute Resume resume) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberService.findByUsername(email);
        resume.setMember(member);

        resumeService.save(resume);
        return "redirect:/member";
    }
}