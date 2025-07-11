package com.multi.matchingbot.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private final CompanyService companyService;
    private final JobService jobService;

    @Autowired
    private AttachedItemService attachedItemService;

    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String companyIndex(@AuthenticationPrincipal MBotUserDetails userDetails, Model model) {
        Long companyId = userDetails.getCompanyId();
        model.addAttribute("companyId", companyId);

        Company company = companyService.findById(companyId);
        model.addAttribute("company", company);

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);

        /*평가 보고서*/
//        Optional<AttachedItem> reportImageOpt =
//                attachedItemService.findReportForCompany(companyId);
//
//        if (reportImageOpt.isPresent()) {
//            AttachedItem report = reportImageOpt.get();
//            String url = "/" + report.getPath();
//            model.addAttribute("reportImageUrl", url);
//        }

        return "company/index";
    }

    @GetMapping("/{id}")
    public CompanyUpdateDto getCompany(@PathVariable("id") Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping
    public CompanyUpdateDto createCompany(@RequestBody CompanyUpdateDto dto) {
        return companyService.createCompany(dto);
    }

    @PutMapping("/{id}")
    public CompanyUpdateDto updateCompany(@PathVariable("id") Long id, @RequestBody CompanyUpdateDto dto) {
        return companyService.updateCompany(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/mypage")
    public String showCompanyMypage(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getCompanyId();
        Company company = companyService.findById(companyId);
        model.addAttribute("company", company); // 이게 있어야 함
        return "company/mypage";
    }

    // 개인정보 수정 페이지
    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getCompanyId();
        model.addAttribute("company", companyService.findById(companyId));
        return "company/edit-profile"; // 실제 템플릿 경로에 맞게
    }

    // 개인정보 수정 처리
    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("phone1") String phone1,
                                @RequestParam("phone2") String phone2,
                                @RequestParam("phone3") String phone3,
                                @ModelAttribute("company") CompanyUpdateDto companyDto,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal MBotUserDetails userDetails) {

        companyDto.setPhone(phone1 + phone2 + phone3);  // 조합하여 저장

        if (bindingResult.hasErrors()) {
            return "company/edit-profile";
        }

        companyService.update(companyDto, userDetails.getCompanyId());
        return "redirect:/company/mypage";
    }

//    // 개인정보 수정 처리
//    @PostMapping("/edit-profile")
//    public String updateProfile(@RequestParam("phone1") String phone1,
//                                @RequestParam("phone2") String phone2,
//                                @RequestParam("phone3") String phone3,
//                                @ModelAttribute("company") CompanyUpdateDto companyDto,
//                                BindingResult bindingResult,
//                                @AuthenticationPrincipal MBotUserDetails userDetails) {
//
//        companyDto.setPhone(phone1 + phone2 + phone3);  // 조합하여 저장
//
//        if (bindingResult.hasErrors()) {
//            return "company/edit-profile";
//        }
//
//        companyService.update(companyDto, userDetails.getCompanyId());
//        return "redirect:/company/mypage";
//    }

}
