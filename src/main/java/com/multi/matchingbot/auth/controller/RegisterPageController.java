package com.multi.matchingbot.auth.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterPageController {

    @GetMapping("/auth/register-user")
    public String showRegisterPage() {
        return "auth/register"; // resources/templates/auth/register.html
    }
}
