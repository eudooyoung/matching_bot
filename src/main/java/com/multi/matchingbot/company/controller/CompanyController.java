package com.multi.matchingbot.company.controller;

import com.multi.matchingbot.company.domain.CompanyDto;
import com.multi.matchingbot.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/")
    public String showCompanyHome(Model model) {
        Long companyId = 1L;

        CompanyDto companyDto = companyService.getCompanyById(companyId);

        model.addAttribute("company_name", companyDto.getName());

        return "company/index";
    }

    @GetMapping("/{id}")
    public CompanyDto getCompany(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto dto) {
        return companyService.createCompany(dto);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto dto) {
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
