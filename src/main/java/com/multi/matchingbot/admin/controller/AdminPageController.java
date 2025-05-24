package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping({"/", "/main"})
    public void mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if (user != null) {
            log.info("userType: {}", user.getRole());
            model.addAttribute("userType", user.getRole());
        } else {
            model.addAttribute("userType", null);
            log.info("비회원 접근");
        }
//        return "admin/main";
    }

    @GetMapping("/login")
    public void login(Model model) {
    }
}
