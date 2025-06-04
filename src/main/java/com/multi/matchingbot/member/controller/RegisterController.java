package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.member.domain.dtos.MemberRegisterDto;
import com.multi.matchingbot.member.service.AuthMemberRegistService;
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
public class RegisterController {

    private final AuthMemberRegistService authMemberRegistService;

    @GetMapping("/register-member")
    public String showRegisterForm(Model model) {
        model.addAttribute("memberDto", new MemberRegisterDto());
        return "auth/register-member";

    }

    @PostMapping("/register-member")
    public String registerUser(@Valid @ModelAttribute("memberDto") MemberRegisterDto dto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // 입력값 검증 실패: 다시 등록 페이지로 이동하면서 에러 출력
            return "auth/register-member";
        }

        try {
            authMemberRegistService.register(dto);
            return "redirect:/auth/login";  // 성공 시 로그인 페이지로 이동
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그
            model.addAttribute("error", e.getMessage());
            return "auth/register-member"; // 실패 시 다시 폼 보여줌
        }

    }
}