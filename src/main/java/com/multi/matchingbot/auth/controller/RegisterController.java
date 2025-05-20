package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.user.MemberService;
import com.multi.matchingbot.user.domain.UserRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class RegisterController {

    private final MemberService memberService;

    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("memberDto", new UserRegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("memberDto") UserRegisterDto dto, Model model) {
        try {
            memberService.register(dto);
            return "redirect:/auth/login";  // 성공 시 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register"; // 실패 시 다시 폼 보여줌
        }
    }
}