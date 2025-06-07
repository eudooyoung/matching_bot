package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeInsertPrefillMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberResumeController {

    private final ResumeService resumeService;
    private final ResumeAdminService resumeAdminService;
    private final MemberService memberService;
    private final OccupationService occupationService;

    @Qualifier("resumeInsertPrefillMapper")
    private final ResumeInsertPrefillMapper prefillMapper;

    /**
     * 이력서 등록 페이지
     *
     * @param userDetails 로그인 사용자 정보
     * @param model       디티오 전달용 객체
     * @return 이력서 목록 페이지 반환
     */
    @GetMapping("/resumes")
    public String list(@AuthenticationPrincipal MBotUserDetails userDetails, Model model) {
        // 로그인 정보 불러오기
        Long memberId = userDetails.getId();
        List<Resume> resumes = resumeService.findByMemberId(memberId);
        model.addAttribute("resumes", resumes);
        return "member/resumes";
    }

    /**
     * 이력서 등록 페이지
     *
     * @return
     */
    @GetMapping("/insert-resume-test")
    public String insertResumeTest(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        ResumeInsertDto dto = prefillMapper.toDto(member);
        dto.splitPhone(dto.getPhone());
        System.out.println(dto);
        model.addAttribute("resumeInsertDto", dto);
        return "member/insert-resume-test";
    }

    @GetMapping("/insert-resume")
    public String insertResume(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        ResumeDto dto = new ResumeDto();
        dto.setMemberId(userDetails.getMemberId());
        model.addAttribute("resume", dto);
        return "member/insert-resume";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Resume resume = resumeService.findByIdWithOccupation(id);
        ResumeDto resumeDto = ResumeDto.fromEntity(resume);
        model.addAttribute("resume", resumeDto);
        return "member/resume-view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "/member/resume-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("resume") ResumeDto dto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member/resume-edit";
        }
        Occupation occupation = occupationService.findById(dto.getOccupationId());
        Resume updatedResume = dto.toEntityWithOccupation(occupation);

        resumeService.update(id, updatedResume);
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

    //이력서 등록 페이지
    /*@GetMapping("/insert")
    public String insertForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        ResumeDto dto = new ResumeDto();
        dto.setMemberId(userDetails.getMemberId());
        model.addAttribute("resume", dto);

        return "member/resume-insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("resume") ResumeDto resumeDto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            System.out.println("📌 Binding Error 발생:");
            bindingResult.getAllErrors().forEach(e -> System.out.println("  - " + e));
            return "member/resume-insert";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
//        Member member = memberService.findByUsername(email);

        Member member = memberService.findById(userDetails.getMemberId());
        Occupation occupation = occupationService.findById(resumeDto.getOccupationId());

        Resume resume = ResumeMapper.toEntity(resumeDto, member, occupation);

        // ✅ 추출된 키워드 문자열 추가 설정
        resume.setSkillKeywords(resumeDto.getSkillKeywordsConcat());
        resume.setTraitKeywords(resumeDto.getTraitKeywordsConcat());

        resumeService.save(resume);

        return "member/member-resume-list";
    }*/
}