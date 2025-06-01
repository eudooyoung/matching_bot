package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.mapper.JobMapper;
import com.multi.matchingbot.job.repository.JobRepository;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.job.service.OccupationService;
import com.multi.matchingbot.job.service.ResumeBookmarkService;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
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

    private final CompanyService companyService;
    private final JobService jobService;
    private final JobRepository jobRepository;
    private final OccupationService occupationService;
    private final ResumeBookmarkService resumeBookmarkService;

    @Autowired
    public JobController(CompanyService companyService, JobService jobService, JobRepository jobRepository, OccupationService occupationService, ResumeBookmarkService resumeBookmarkService) {
        this.companyService = companyService;
        this.jobService = jobService;
        this.jobRepository = jobRepository;
        this.occupationService = occupationService;
        this.resumeBookmarkService = resumeBookmarkService;
    }

    // 공고 목록
    @GetMapping("/manage-jobs")
    public String showManageJobsPage(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MBotUserDetails userDetails = (MBotUserDetails) authentication.getPrincipal();
        Long companyId = userDetails.getCompanyId();

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(page, size));
        model.addAttribute("jobPage", jobPage);

        return "job/manage-jobs";
    }

    // 공고 등록 페이지
    @GetMapping("/new")
    public String showForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        JobDto dto = new JobDto();
        dto.setCompanyId(userDetails.getCompanyId());
        model.addAttribute("job", dto);

        return "job/job-new";
    }

    // 공고 등록 처리
    @PostMapping("/new")
    public String registerJob(@Valid @ModelAttribute("job") JobDto jobDto,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            System.out.println("📌 Binding Error 발생:");
            bindingResult.getAllErrors().forEach(e -> System.out.println("  - " + e));
            return "job/job-new";
        }

        // System.out.println("Principal (로그인된 사용자): " + userDetails.getCompanyId());

        Company company = companyService.findById(userDetails.getCompanyId());
        // System.out.println("조회된 CompanyId: " + company);

        // System.out.println("넘어온 occupationId: " + jobDto.getOccupationId());
        Occupation occupation = occupationService.findById(jobDto.getOccupationId());
        // System.out.println("조회된 Occupation: " + occupation);

        // System.out.println("등록할 JobDto 정보:");
        // System.out.println(jobDto);

        Job job = JobMapper.toEntity(jobDto, company, occupation);

        // System.out.println("실제 저장될 Job entity:");
        // System.out.println(job);

        jobRepository.save(job);

        // System.out.println("✅ 저장 성공!");
        return "redirect:/job/manage-jobs";
    }


    // 공고 수정 페이지
//    @GetMapping("/{id:[0-9]+}/edit")
//    public String editJobForm(@PathVariable("id") Long id, Model model) {
//        JobDto dto = jobService.getById(id);
//        model.addAttribute("job", dto);
//        return "job/job-edit";
//    }
        @GetMapping("/{id}/edit")
        public String showEditForm(@PathVariable("id") Long id, Model model) {
            Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
            JobDto dto = JobMapper.toDto(job);
            model.addAttribute("job", dto);

            model.addAttribute("occupationId", dto.getOccupationId());

            return "job/job-edit";
        }

    // 공고 수정 처리
    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("job") JobDto dto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "job/job-edit";
        }

        Occupation occupation = occupationService.findById(dto.getOccupationId());
        Job updatedJob = dto.toEntityWithOccupation(occupation);

        jobService.update(id, updatedJob);

        return "redirect:/job/manage-jobs";
    }

    // 공고 상세 보기
    @GetMapping("/{id}")
    public String getJobDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getCompanyId();

        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("companyId", companyId);

        return "job/job-detail";
    }

    // 관심 이력서 관리(id 포함)
    @GetMapping("/resume-bookmark")
    public String showResumeBookmarkPage(@AuthenticationPrincipal MBotUserDetails userDetails, Model model,
                                         @PageableDefault(size = 10) Pageable pageable) {
        Long companyId = userDetails.getCompanyId();
        model.addAttribute("companyId", companyId);

        Page<ResumeDto> resumePage = resumeBookmarkService.getBookmarkedResumes(companyId, pageable); // ✅ 변경된 쿼리
        model.addAttribute("resumePage", resumePage);

        return "job/resume-bookmark";
    }
}