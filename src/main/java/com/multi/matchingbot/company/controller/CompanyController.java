package com.multi.matchingbot.company.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private final CompanyService companyService;
    private final JobService jobService;

    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String companyIndex(@AuthenticationPrincipal MBotUserDetails userDetails, Model model) {
        Long companyId = userDetails.getId(); // 로그인한 회사 ID
        model.addAttribute("companyId", companyId);

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);

        return "company/index";
    }

    @GetMapping("/{id}")
    public CompanyUpdateDto getCompany(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping
    public CompanyUpdateDto createCompany(@RequestBody CompanyUpdateDto dto) {
        return companyService.createCompany(dto);
    }

    @PutMapping("/{id}")
    public CompanyUpdateDto updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateDto dto) {
        return companyService.updateCompany(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/mypage")
    public String mypageHome() {
        return "company/mypage";
    }

    // 개인정보 수정 페이지
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getId();
        model.addAttribute("company", companyService.findById(companyId));
        return "company/edit-profile"; // 실제 템플릿 경로에 맞게
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@Valid @ModelAttribute("company") CompanyUpdateDto companyDto,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal MBotUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "company/edit-profile";
        }

        companyService.update(companyDto, userDetails.getId());
        return "redirect:/job/mypage";
    }

}
