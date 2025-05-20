package com.multi.matchingbot.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping({"/", "/main"})
    public String mainPage(Model model) {
        return "main/main";
    }

    @GetMapping("/admin/login")
    public void adminLogin(Model model) {
    }

}
