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
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Resume;
import com.multi.matchingbot.member.service.JobBookmarkService;
import com.multi.matchingbot.member.service.ResumeService;
import com.multi.matchingbot.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    private final CompanyService companyService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final JobRepository jobRepository;
    private final OccupationService occupationService;
    private final ResumeBookmarkService resumeBookmarkService;
    private final ResumeService resumeService;
    private final JobBookmarkService jobBookmarkService;

    @Autowired
    public JobController(CompanyService companyService, JobService jobService, NotificationService notificationService, JobRepository jobRepository, OccupationService occupationService, ResumeBookmarkService resumeBookmarkService, ResumeService resumeService, JobBookmarkService jobBookmarkService) {
        this.companyService = companyService;
        this.jobService = jobService;
        this.notificationService = notificationService;
        this.jobRepository = jobRepository;
        this.occupationService = occupationService;
        this.resumeBookmarkService = resumeBookmarkService;
        this.resumeService = resumeService;
        this.jobBookmarkService = jobBookmarkService;
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

        Company company = companyService.findById(userDetails.getCompanyId());
        Occupation occupation = occupationService.findById(jobDto.getOccupationId());
        Job job = JobMapper.toEntity(jobDto, company, occupation);

        job.setSkillKeywords(jobDto.getSkillKeywordsConcat());
        job.setTraitKeywords(jobDto.getTraitKeywordsConcat());

        jobService.createJob(job); // 위도, 경도 불러서 저장

        notificationService.sendJobNotificationToBookmarkedMembers(
                company.getId(),
                company.getName(),
                job.getTitle()
        );

        return "redirect:/job/manage-jobs";
    }

    // 공고 수정 페이지
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

        updatedJob.setSkillKeywords(dto.getSkillKeywordsConcat());
        updatedJob.setTraitKeywords(dto.getTraitKeywordsConcat());

        jobService.update(id, updatedJob);

        return "redirect:/job/manage-jobs";
    }

    // 공고 상세보기 형찬코드
    @GetMapping("/{id}")
    public String getJobDetail(@PathVariable("id") Long id,
                               Model model,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {

        String role = null;
        Resume resume = null;
        if (userDetails != null) {
            role = userDetails.getRole().name();

            // 이력서 조회
            List<Resume> resumes = resumeService.findByMemberId(userDetails.getMemberId());
            if (!resumes.isEmpty()) {
                resume = resumes.get(0); // 첫 번째 이력서 사용
            }

            model.addAttribute("resume", resume); // ✅ 비회원이면 이 값은 안 들어감

            // 구직자인 경우 북마크 상태 전달
            if ("MEMBER".equals(userDetails.getRole())) {
                Long memberId = userDetails.getMemberId();
                boolean isJobBookmarked = jobBookmarkService.isBookmarked(memberId, id);
                model.addAttribute("isJobBookmarked", isJobBookmarked);
            } else {
                model.addAttribute("isJobBookmarked", false);
            }
        } else {
            model.addAttribute("isJobBookmarked", false);
        }

        Job job = jobService.findById(id);
        Long postingCompanyId = job.getCompany().getId();

        model.addAttribute("job", job);
        model.addAttribute("resume", resume);
        model.addAttribute("role", role);
        model.addAttribute("companyId", postingCompanyId);

        return "job/job-detail";
    }

    // 채용공고 북마크 추가/제거 토글 API
    @PostMapping("/api/job-bookmark/toggle")
    @ResponseBody
    public ResponseEntity<String> toggleJobBookmark(@RequestParam("jobId") Long jobId,
                                                    @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();

        if (jobBookmarkService.isBookmarked(memberId, jobId)) {
            jobBookmarkService.removeJobBookmark(memberId, jobId);
            return ResponseEntity.ok("removed");
        } else {
            jobBookmarkService.addJobBookmark(memberId, jobId);
            return ResponseEntity.ok("added");
        }
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