package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.member.domain.dto.MemberRegisterDto;
import com.multi.matchingbot.member.service.MemberRegisterService;
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
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRegisterController {

    private final MemberRegisterService registerService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setYear(2000); // 원하는 기본 연도 (예: 2000년생)
        model.addAttribute("memberDto", dto);
        return "member/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("memberDto") MemberRegisterDto dto, BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    System.out.println("Field error in: " + error.getField() + " - " + error.getDefaultMessage())
            );
            return "member/register";
        }

        try {
            registerService.registerMember(dto);
            return "redirect:/auth/login";  // 성공 시 로그인 페이지로 이동
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그
            model.addAttribute("memberDto", dto);
            model.addAttribute("error", e.getMessage());
            return "member/register"; // 실패 시 다시 폼 보여줌
        }
    }
}
