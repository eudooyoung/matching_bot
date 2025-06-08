package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeInsertPrefillMapper;
import com.multi.matchingbot.resume.mapper.ResumeUpdatePrefillMapper;
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
    private final ResumeInsertPrefillMapper insertPrefillMapper;
    @Qualifier("resumeUpdatePrefillMapper")
    private final ResumeUpdatePrefillMapper updatePrefillMapper;

    /**
     * 이력서 관리 페이지 매핑
     *
     * @param userDetails 로그인 사용자 정보
     * @param model       resume 리스트 전달
     * @return 이력서 관리 페이지 반환
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
     * 이력서 등록 페이지 매핑
     *
     * @param model       회원 인적 사항(resumeInsertDto) 전달
     * @param userDetails 로그인 정보
     * @return 등록 페이지 반환
     */
    @GetMapping("/insert-resume")
    public String insertResume(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        ResumeInsertDto dto = insertPrefillMapper.toDto(member);
        dto.splitPhone(dto.getPhone());
        model.addAttribute("resumeInsertDto", dto);
        return "member/insert-resume";
    }

    /**
     * 이력서 등록 메소드
     *
     * @param dto           이력서 등록용 dto
     * @param bindingResult 유효성 검사 객체
     * @param model         이력서 등록 실패시 dto 재전달
     * @param userDetails   로그인 정보
     * @return 성공시 이력서 목록 페이지 리다이렉트, 실패시 이동 없음
     */
    @PostMapping("/insert-resume")
    public String insertResume(@Valid @ModelAttribute ResumeInsertDto dto, BindingResult bindingResult, Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            log.warn("❌ 이력서 입력 오류: {}", bindingResult.getAllErrors());
            model.addAttribute("resumeInsertDto", dto);
            return "member/insert-resume"; // 다시 입력 화면으로
        }

        log.info("📨 이력서 등록 요청: {}", dto);
        dto.mergePhone();
        Member member = memberService.findById(userDetails.getId());
        resumeService.insertResume(dto, member);
        return "redirect:/member/resumes";
    }

    /**
     * 이력서 수정 페이지 매핑
     *
     * @param id          이력서 아이디
     * @param model       기존 이력서 정보(resumeUpdateDto) 전달
     * @param userDetails 로그인 정보
     * @return 이력서 수정 페이지 이동
     */
    @GetMapping("/update-resume/{id}")
    public String updateResumeForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        Resume resume = resumeService.findByIdAndMemberWithOccupation(id, member);
        ResumeUpdateDto dto = updatePrefillMapper.toDto(resume); // → prefill mapper 필요

        dto.splitPhone(dto.getPhone()); // phone1~3 나누기
        System.out.println(dto);
        System.out.println(dto.getCareers().toString());

        model.addAttribute("resumeUpdateDto", dto);
        return "member/update-resume";
    }

    /**
     * @param dto           수정된 이력서 정보
     * @param bindingResult 유효성 검사용 객체
     * @param model         이력서 수정 실패시 dto를 재전달
     * @param userDetails   로그인 정보
     * @return 성공시 이력서 관리페이지로 이동, 실패시 이동 없음
     */
    @PostMapping("/update-resume")
    public String updateResume(@Valid @ModelAttribute ResumeUpdateDto dto, BindingResult bindingResult, Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println(err));
            model.addAttribute("resumeUpdateDto", dto);
            return "member/update-resume";
        }

        Member member = memberService.findById(userDetails.getId());
        resumeService.updateResume(dto, member);

        return "redirect:/member/resumes"; // 혹은 상세 페이지
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Resume resume = resumeService.findByIdWithOccupation(id);
        ResumeDto resumeDto = ResumeDto.fromEntity(resume);
        model.addAttribute("resume", resumeDto);
        return "member/resume-view";
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