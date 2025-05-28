package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.JobDto;
import com.multi.matchingbot.job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final CompanyService companyService;

    @Autowired
    public JobController(JobService jobService, CompanyService companyService) {
        this.jobService = jobService;
        this.companyService = companyService;
    }

    // 공고 목록
    @GetMapping("/manage-jobs")
    public String showManageJobsPage(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {

        Page<JobDto> jobPage = jobService.getAllPaged(PageRequest.of(page, size));
        model.addAttribute("jobPage", jobPage);
        return "job/manage-jobs";
    }

    // 관심 이력서 페이지
    @GetMapping("/resume-list")
    public String showResumeListPage() {
        return "job/resume-list";
    }

    // 개인 정보 수정 페이지
//    @GetMapping("/edit-profile")
//    public String showEditProfile(Model model) {
//        CompanyUpdateDto company = companyService.findByUserEmail();
//        model.addAttribute("company", company);
//        return "job/edit-profile"; // 폴더 경로 포함
//    }

//    @PostMapping("/edit-profile")
//    public String updateProfile(@Valid @ModelAttribute("company") CompanyUpdateDto companyDto,
//                                BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "job/edit-profile";
//        }
//
//        companyService.update(companyDto);
//        return "redirect:/job/edit-profile";
//    }

    // 공고 등록 페이지
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("job", new JobDto());
        return "job/job-new";
    }

    // 공고 등록 처리 (POST)
    @PostMapping("/new")
    public String save(@Valid @ModelAttribute("job") JobDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "job/job-new";
        }

        jobService.save(dto);

        return "redirect:/job/manage-jobs";
    }

//    @PostMapping("/new")
//    public String save(@ModelAttribute JobPostingDto dto) {
//        dto.setCompanyId(1L); // ✅ 반드시 companyId 설정!
//        jobPostingService.save(dto);
//        return "redirect:/job/manage_jobs";
//    }

    // 공고 상세보기
    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        JobDto job = jobService.getById(id);
        model.addAttribute("job", job);
        return "job/job-detail";
    }

    // 공고 수정 페이지
    @GetMapping("/{id}/edit")
    public String editJobForm(@PathVariable("id") Long id, Model model) {
        JobDto dto = jobService.getById(id);
        model.addAttribute("job", dto);
        return "job/job-edit";
    }

    // 공고 수정 처리 (POST)
    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("job") JobDto dto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "job/job-edit";
        }

        jobService.update(id, dto);
        return "redirect:/job/manage_jobs";
    }

    // 공고 삭제 처리
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteJob(@PathVariable("id") Long id) {
        jobService.delete(id);
    }
}