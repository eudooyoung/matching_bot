package com.multi.matchingbot.jobposting.controller;

import com.multi.matchingbot.jobposting.model.dto.JobPostingDto;
import com.multi.matchingbot.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showManageJobsPage(Model model) {
        List<JobPostingDto> jobList = jobPostingService.getAll();
        model.addAttribute("jobList", jobList);
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
    @PostMapping("/new")
    public String save(@ModelAttribute JobPostingDto dto) {
        dto.setCompanyId(1L); // 테스트용 company ID
        jobPostingService.save(dto);
        return "redirect:/job/manage_jobs";
    }

    // 공고 상세보기 (숫자 ID만 허용)
    @GetMapping("/{id:[0-9]+}")
    public String showDetail(@PathVariable Long id, Model model) {
        JobPostingDto job = jobPostingService.getById(id);
        model.addAttribute("job", job);
        return "job/job_detail";
    }

    // 공고 수정 페이지
    @GetMapping("/{id}/edit")
    public String editJobForm(@PathVariable Long id, Model model) {
        JobPostingDto dto = jobPostingService.getById(id);
        model.addAttribute("job", dto);
        return "job/job_edit";
    }

    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobPostingDto dto) {
        jobPostingService.update(id, dto);
        return "redirect:/job/manage_jobs";
    }

    // 공고 삭제 처리
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteJob(@PathVariable Long id) {
        jobPostingService.delete(id);
    }
}