package com.multi.matchingbot.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthPageController {

    /*@GetMapping("/register-user")
    public String showRegisterPage() {
        return "auth/register"; // resources/templates/auth/register.html
    }*/

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/logout")
    public void logout() {
    }
}
