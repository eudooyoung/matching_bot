package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.admin.service.ResumeAdminService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.domain.entity.Resume;
import com.multi.matchingbot.member.mapper.ResumeMapper;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/member")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeAdminService resumeAdminService;
    private final MemberService memberService;
    private final OccupationService occupationService;
    private final JobService jobService;

    @Autowired
    public ResumeController(ResumeService resumeService, ResumeAdminService resumeAdminService, MemberService memberService, OccupationService occupationService, JobService jobService){
        this.resumeService = resumeService;
        this.resumeAdminService = resumeAdminService;
        this.memberService = memberService;
        this.occupationService = occupationService;
        this.jobService = jobService;
    }

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
    @GetMapping("/insert")
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
    }

    // 이력서 상세보기
    @GetMapping("/{id}")
    public String getResumeDetail(@PathVariable("id") Long id,
                               Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {

        String role = null;
        List<Job> jobs = Collections.emptyList(); // 기본값
        System.out.println("userDetails: " + userDetails);
        if (userDetails != null) {
            role = userDetails.getRole().name();
            System.out.println("role = " + role);

            jobs = jobService.findByCompanyId(userDetails.getMemberId());
            model.addAttribute("jobs", jobs);
        }
        Resume resume = resumeService.findById(id);
        Long postingMemberId = resume.getMember().getId();

        model.addAttribute("resume", resume);
        model.addAttribute("role", role);
        model.addAttribute("companyId", postingMemberId);

        return "member/resume-view";
    }
}