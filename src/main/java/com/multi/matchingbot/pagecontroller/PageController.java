package com.multi.matchingbot.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping({"/", "/main"})
    public String mainPage(Model model) {
        return "main/main";
    }

    @GetMapping("/admin/login")
    public void adminLogin(Model model) {
    }

    @GetMapping("/auth/register")
    public void Login(Model model) {
    }
}
