package com.multi.matchingbot.auth.AuthCompany;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CompanyRegisterController {

    private final AuthCompanyRegistService authCompanyRegistService;

    @GetMapping("/register-company")
    public String showCompanyRegisterForm(Model model) {
        model.addAttribute("companyDto", new CompanyRegisterDto());

        return "auth/register-company";

    }

    @PostMapping("/register-company")
    public String registerCompany(@ModelAttribute("companyDto") CompanyRegisterDto dto, Model model) {
        try {
            authCompanyRegistService.register(dto);
            return "redirect:/auth/login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "auth/register-company";
        }
    }

}