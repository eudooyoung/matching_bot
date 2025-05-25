package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final UserRepository userRepository;

    @GetMapping({"/", "/main", ""})
    public String mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if (user != null) {
            log.info("userType: {}", user.getRole());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("role", null);
            log.info("비회원 접근");
        }
        return "main/main";
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/users")
    public void users(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        Pageable pageable = PageRequest.of(0,20);
        model.addAttribute("users", userRepository.findAll(pageable));
//        model.addAttribute("user", user)
    }
}
