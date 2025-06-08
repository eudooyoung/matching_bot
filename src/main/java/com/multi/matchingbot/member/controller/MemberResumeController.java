package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import com.multi.matchingbot.resume.domain.dto.CareerDto;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeInsertPrefillMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
     * @param model       디티오 전달용 객체
     * @param userDetails 로그인 정보
     * @return
     */
    @GetMapping("/insert-resume")
    public String insertResume(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        ResumeInsertDto dto = prefillMapper.toDto(member);
        dto.splitPhone(dto.getPhone());
        System.out.println(dto);
        model.addAttribute("resumeInsertDto", dto);
        return "member/insert-resume";
    }


    /**
     * 이력서 등록 메소드
     * @param dto 이력서 등록용 dto
     * @param bindingResult 유효성 검사 객체
     * @param userDetails 로그인 정보
     * @return 이력서 목록 페이지 리다이렉트
     */
    @PostMapping("/insert-resume")
    public String insertResume(@Valid @ModelAttribute ResumeInsertDto dto, BindingResult bindingResult,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            log.warn("❌ 이력서 입력 오류: {}", bindingResult.getAllErrors());
            return "member/insert-resume"; // 다시 입력 화면으로
        }

        log.info("📨 이력서 등록 요청: {}", dto);
        dto.mergePhone();

        for (int i = 0; i < dto.getCareers().size(); i++) {
            CareerDto c = dto.getCareers().get(i);
            log.warn("▶️ Career {} - 회사명: {}", i, c.getCompanyName());
        }

        Member member = memberService.findById(userDetails.getId());

        // 저장
        resumeService.insertResume(dto, member);

        return "redirect:/member/resumes";
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


}