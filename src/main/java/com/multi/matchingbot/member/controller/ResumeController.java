/*
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
*/
