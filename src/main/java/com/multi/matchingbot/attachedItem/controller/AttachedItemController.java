package com.multi.matchingbot.attachedItem.controller;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attached")
@RequiredArgsConstructor
public class AttachedItemController {

    private final JobService jobService;
    private final CompanyService companyService;

    @GetMapping("/company/{postingCompanyId}")
    public String goPostingCompanyHome(@PathVariable("postingCompanyId") Long postingCompanyId, Model model) {
        model.addAttribute("companyId", postingCompanyId);

        Company company = companyService.findById(postingCompanyId);
        model.addAttribute("company", company);

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(postingCompanyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);
        return "company/index";
    }
}
