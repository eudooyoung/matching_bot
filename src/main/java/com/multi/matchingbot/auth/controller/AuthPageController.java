package com.multi.matchingbot.auth.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPageController {

    @GetMapping("/register-user")
    public String showRegisterPage() {
        return "auth/register"; // resources/templates/auth/register.html
    }

    @GetMapping("/login")
    public void login(Model model) {
    }

    @GetMapping("/register-company")
    public String showCompanyRegisterPage() {
        return "auth/register-company";
    }
}
