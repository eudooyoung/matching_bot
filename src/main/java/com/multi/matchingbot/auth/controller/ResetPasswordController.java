package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.auth.domain.dto.ResetPasswordDto;
import com.multi.matchingbot.auth.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @GetMapping
    public String showResetPage(@RequestParam("email") String email,
                                @RequestParam("userType") String userType,
                                Model model) {
        model.addAttribute("email", email);
        model.addAttribute("userType", userType);
        return "auth/reset-password";
    }

    @PostMapping
    public String processReset(@ModelAttribute ResetPasswordDto dto, Model model) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("email", dto.getEmail());
            model.addAttribute("userType", dto.getUserType());
            return "auth/reset-password";
        }

        boolean result;
        if ("company".equals(dto.getUserType())) {
            result = resetPasswordService.resetCompanyPassword(dto);
        } else {
            result = resetPasswordService.resetPassword(dto);
        }

        if (result) {
            return "redirect:/auth/login?resetSuccess";
        } else {
            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
            model.addAttribute("email", dto.getEmail());
            model.addAttribute("userType", dto.getUserType());
            return "auth/reset-password";
        }
    }
}