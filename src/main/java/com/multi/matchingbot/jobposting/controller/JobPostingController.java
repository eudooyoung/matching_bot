package com.multi.matchingbot.jobposting.controller;

import com.multi.matchingbot.jobposting.model.dto.JobPostingDto;
import com.multi.matchingbot.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping("/manage_jobs")
    public String showManageJobsPage(Model model) {
        List<JobPostingDto> jobList = jobPostingService.getAll();
        model.addAttribute("jobList", jobList);
        return "job/manage_jobs";
    }

    @GetMapping("/resume_list")
    public String showResumeListPage() {
        return "job/resume_list";
    }

    @GetMapping("/profile_edit")
    public String showEditProfilePage() {
        return "job/edit_profile";
    }

//    @GetMapping("/{id:[0-9]+}")
//    public String viewJob(@PathVariable Long id, Model model) {
//        JobPostingDto dto = jobPostingService.getById(id);
//        model.addAttribute("job", dto);
//        return "joby/job_detail";
//    }
//
//    @GetMapping("/{id:[0-9]+}/edit")
//    public String editJobForm(@PathVariable Long id, Model model) {
//        JobPostingDto dto = jobPostingService.getById(id);
//        model.addAttribute("job", dto);
//        return "job/job_edit";
//    }
//
//    @DeleteMapping("/{id:[0-9]+}")
//    @ResponseBody
//    public void deleteJob(@PathVariable Long id) {
//        jobPostingService.delete(id);
//    }
}
