package com.multi.matchingbot.company.controller;

import com.multi.matchingbot.company.service.AuthCompanyRegistService;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
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
    public String registerCompany(@Valid @ModelAttribute("companyDto") CompanyRegisterDto dto,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            // 입력값 검증 실패: 다시 등록 페이지로 이동하면서 에러 출력
            return "auth/register-company";
        }

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