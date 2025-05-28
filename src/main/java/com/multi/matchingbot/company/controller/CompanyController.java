package com.multi.matchingbot.company.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /*@GetMapping("/")
    public String showCompanyHome(Model model) {
        Long companyId = 1L;

        CompanyDto companyDto = companyService.getCompanyById(companyId);

        model.addAttribute("company_name", companyDto.getName());

        return "company/index";
    }*/

    @GetMapping("/")
    public String showCompanyHome(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getId(); // 로그인한 기업회원의 ID
        CompanyUpdateDto companyUpdateDto = companyService.getCompanyById(companyId);

        model.addAttribute("company_name", companyUpdateDto.getName());
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

}
