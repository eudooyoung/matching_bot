package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberResumeService;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.resume.domain.dto.ResumeDetailDto;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.mapper.ResumeDetailMapper;
import com.multi.matchingbot.resume.mapper.ResumeInsertPrefillMapper;
import com.multi.matchingbot.resume.mapper.ResumeUpdatePrefillMapper;
import com.multi.matchingbot.resume.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MemberResumeService memberResumeService;
    private final MemberService memberService;
    private final ResumeDetailMapper resumeDetailMapper;

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
    @GetMapping("/manage-resumes")
    public String list(@AuthenticationPrincipal MBotUserDetails userDetails, Model model) {
        // 로그인 정보 불러오기
        Long memberId = userDetails.getId();
        List<Resume> resumes = resumeService.findByMemberId(memberId);
        model.addAttribute("resumes", resumes);
        return "member/manage-resumes";
    }

    /**
     * 이력서 등록 페이지 매핑
     *
     * @param model       회원 인적 사항(resumeInsertDto) 전달
     * @param userDetails 로그인 정보
     * @return 등록 페이지 반환
     */
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/insert-resume")
    public String insertResume(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        ResumeInsertDto dto = insertPrefillMapper.toDto(member);
        dto.splitPhone(dto.getPhone());
        model.addAttribute("resumeInsertDto", dto);
        return "member/insert-resume";
    }

    /**
     * 이력서 등록 컨트롤러 메소드
     *
     * @param dto           이력서 등록용 dto
     * @param bindingResult 유효성 검사 객체
     * @param model         이력서 등록 실패시 dto 재전달
     * @param userDetails   로그인 정보
     * @return 성공시 이력서 목록 페이지 리다이렉트, 실패시 이동 없음
     */
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/insert-resume")
    public String insertResume(@Valid @ModelAttribute ResumeInsertDto dto, BindingResult bindingResult, Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {
            log.warn("❌ 이력서 유효성 오류: {}", bindingResult.getAllErrors());

            // 👉 커리어는 JS로 렌더링되기 때문에,
            //    개별 필드 메시지 대신 글로벌 에러 메시지 하나 추가 (선택)
            bindingResult.reject("careerInvalid", "경력 항목에 누락된 정보가 있습니다.");

            model.addAttribute("resumeInsertDto", dto);
            return "member/insert-resume";
        }

        log.info("📨 이력서 등록 요청: {}", dto);
        dto.mergePhone();

        Member member = memberService.findById(userDetails.getId());
        memberResumeService.insertResume(dto, member);

        return "redirect:/member/manage-resumes";
    }

    /**
     * 이력서 수정 페이지 매핑
     *
     * @param id          이력서 아이디
     * @param model       기존 이력서 정보(resumeUpdateDto) 전달
     * @param userDetails 로그인 정보
     * @return 이력서 수정 페이지 이동
     */
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/update-resume/{id}")
    public String updateResumeForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        Resume resume = memberResumeService.findByIdAndMemberWithOccupation(id, member);
        ResumeUpdateDto dto = updatePrefillMapper.toDto(resume); // → prefill mapper 필요

        dto.splitPhone(dto.getPhone()); // phone1~3 나누기
        System.out.println(dto);
        System.out.println(dto.getCareers().toString());

        model.addAttribute("resumeUpdateDto", dto);
        return "member/update-resume";
    }

    /**
     * 이력서 수정 컨트롤러 메소드
     *
     * @param dto           수정된 이력서 정보
     * @param bindingResult 유효성 검사용 객체
     * @param model         이력서 수정 실패시 dto를 재전달
     * @param userDetails   로그인 정보
     * @return 성공시 이력서 관리페이지로 이동, 실패시 이동 없음
     */
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/update-resume")
    public String updateResume(@Valid @ModelAttribute ResumeUpdateDto dto, BindingResult bindingResult, Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println(err));
            model.addAttribute("resumeUpdateDto", dto);
            return "member/update-resume";
        }

        Member member = memberService.findById(userDetails.getId());
        dto.mergePhone();
        memberResumeService.updateResume(dto, member);

        return "redirect:/member/manage-resumes";
    }

    /**
     * 멤버용 이력서 조회 페이지
     *
     * @param id
     * @param userDetails
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/resume/{id}")
    public String viewMyResume(@PathVariable("id") Long id, @AuthenticationPrincipal MBotUserDetails userDetails,
                               Model model) {

        Resume resume = memberResumeService.findByIdWithAll(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서입니다."));

        if (!resume.getMember().getId().equals(userDetails.getId())) {
            throw new AccessDeniedException("본인의 이력서만 조회할 수 있습니다.");
        }

        ResumeDetailDto resumeDetailDto = resumeDetailMapper.toDto(resume);
        model.addAttribute("resume", resumeDetailDto);
        return "resume/detail";
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