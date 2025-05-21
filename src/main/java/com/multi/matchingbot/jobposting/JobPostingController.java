package com.multi.matchingbot.jobposting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/job-postings")
public class JobPostingController {

    @GetMapping("/manage-jobs")
    public String showManageJobsPage() {
        return "company/manage_jobs";
    }

    @GetMapping("/resume-list")
    public String showResumeListPage() {
        return "company/resume_list";
    }

//    @GetMapping("/edit-profile")
//    public String showEditProfilePage() {
//        return "company/edit_profile";
//    }

}
