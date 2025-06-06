package com.multi.matchingbot.company.controller;

import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import com.multi.matchingbot.company.service.CompanyRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyRegisterController {

    private final CompanyRegisterService registerService;

    @GetMapping("/register")
    public String showCompanyRegisterForm(Model model) {
        CompanyRegisterDto dto = new CompanyRegisterDto();
        dto.setYearFound(1980);
        model.addAttribute("companyDto", dto);
        return "company/register";
    }

    @PostMapping("/register")
    public String registerCompany(@Valid @ModelAttribute("companyDto") CompanyRegisterDto dto, BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("companyDto", dto); // <- 입력 값 유지
            return "company/register";
        }

        try {
            registerService.registerCompany(dto);
            return "redirect:/auth/login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "company/register";
        }

    }
}
