package com.multi.matchingbot.jobposting.controller;

import com.multi.matchingbot.jobposting.domain.JobPostingDto;
import com.multi.matchingbot.jobposting.service.JobPostingService;
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
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Autowired
    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    // 공고 목록
    @GetMapping("/manage_jobs")
    public String showManageJobsPage(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {

        Page<JobPostingDto> jobPage = jobPostingService.getAllPaged(PageRequest.of(page, size));
        model.addAttribute("jobPage", jobPage);
        return "job/manage_jobs";
    }

    // 관심 이력서 페이지
    @GetMapping("/resume_list")
    public String showResumeListPage() {
        return "job/resume_list";
    }

    // 개인정보 수정 페이지
    @GetMapping("/profile_edit")
    public String showEditProfilePage() {
        return "job/edit_profile";
    }

    // 공고 등록 페이지
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("job", new JobPostingDto());
        return "job/job_new";
    }

    // 공고 등록 처리 (POST)
//    @PostMapping("/new")
//    public String save(@Valid @ModelAttribute("job") JobPostingDto dto,
//                       BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "job/job_new";
//        }
//
//        jobPostingService.save(dto);
//        return "redirect:/job/manage_jobs";
//    }

    @PostMapping("/new")
    public String save(@ModelAttribute JobPostingDto dto) {
        dto.setCompanyId(1L); // ✅ 반드시 companyId 설정!
        jobPostingService.save(dto);
        return "redirect:/job/manage_jobs";
    }

    // 공고 상세보기
    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        JobPostingDto job = jobPostingService.getById(id);
        model.addAttribute("job", job);
        return "job/job_detail";
    }

    // 공고 수정 페이지
    @GetMapping("/{id}/edit")
    public String editJobForm(@PathVariable("id") Long id, Model model) {
        JobPostingDto dto = jobPostingService.getById(id);
        model.addAttribute("job", dto);
        return "job/job_edit";
    }

    // 공고 수정 처리 (POST)
    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("job") JobPostingDto dto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "job/job_edit";
        }

        jobPostingService.update(id, dto);
        return "redirect:/job/manage_jobs";
    }

    // 공고 삭제 처리
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteJob(@PathVariable("id") Long id) {
        jobPostingService.delete(id);
    }
}