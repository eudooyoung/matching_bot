package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final ResumeService resumeService;

    @Autowired
    public JobController(JobService jobService, ResumeService resumeService) {
        this.jobService = jobService;
        this.resumeService = resumeService;
    }

    // 공고 목록
    @GetMapping("/manage-jobs")
    public String showManageJobsPage(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MBotUserDetails userDetails = (MBotUserDetails) authentication.getPrincipal();
        Long companyId = userDetails.getId();

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(page, size));
        model.addAttribute("jobPage", jobPage);

        return "job/manage-jobs";
    }

    // 공고 등록 페이지
    @GetMapping("/new")
    public String showForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        JobDto dto = new JobDto();
        dto.setCompanyId(userDetails.getId());
        model.addAttribute("job", dto);
        return "job/job-new";
    }

    // 공고 등록 처리
    @PostMapping("/new")
    public String save(@Valid @ModelAttribute("job") JobDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "job/job-new";
        }

        jobService.save(dto);
        return "redirect:/job/manage-jobs";
    }

    // 공고 수정 페이지
    @GetMapping("/{id:[0-9]+}/edit")
    public String editJobForm(@PathVariable("id") Long id, Model model) {
        JobDto dto = jobService.getById(id);
        model.addAttribute("job", dto);
        return "job/job-edit";
    }

    // 공고 수정 처리
    @PostMapping("/{id:[0-9]+}/edit")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("job") JobDto dto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "job/job-edit";
        }

        jobService.update(id, dto);
        return "redirect:/job/manage-jobs";
    }

    // 공고 삭제 처리
    @DeleteMapping("/{id:[0-9]+}")
    @ResponseBody
    public void deleteJob(@PathVariable("id") Long id) {
        jobService.delete(id);
    }

    // 공고 상세 보기
    @GetMapping("/{id:[0-9]+}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        JobDto job = jobService.getById(id);
        model.addAttribute("job", job);
        return "job/job-detail";
    }

    // 관심 이력서 관리(id 포함)
//    @GetMapping("/resume-bookmark")
//    public String getBookmarkedResumes(Model model, @AuthenticationPrincipal MBotUserDetails mBotUserDetails) {
//        Long companyId = mBotUserDetails.getCompanyId();
//        Page<Resume> resumePage = resumeBookmarkService.getBookmarkedResumes(companyId, PageRequest.of(0, 10));
//        model.addAttribute("resumePage", resumePage); // ✅ 이게 꼭 있어야 함
//        return "job/resume-bookmark";
//    }

    // 관심 이력서 관리
    @GetMapping("/resume-bookmark")
    public String showResumeBookmarkPage(Model model,
                                         @PageableDefault(size = 10) Pageable pageable) {
        Page<ResumeDto> resumePage = resumeService.getAllResumes(pageable);
        model.addAttribute("resumePage", resumePage);
        return "job/resume-bookmark";
    }
}