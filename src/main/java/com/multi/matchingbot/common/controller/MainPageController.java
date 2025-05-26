package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Slf4j
public class MainPageController {

    @GetMapping({"/", "/main"})
    public String mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if(user != null) {
            log.info("role: {}", user.getRole());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("role", null);
            log.info("비회원 접근");
        }
        return "main/main";
    }


}
