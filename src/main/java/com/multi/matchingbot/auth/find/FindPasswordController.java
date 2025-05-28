package com.multi.matchingbot.auth.find;

import com.multi.matchingbot.auth.find.dto.FindCompanyPasswordDto;
import com.multi.matchingbot.auth.find.dto.FindUserPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/find-password")
@RequiredArgsConstructor
public class FindPasswordController {

    private final FindPasswordService findPasswordService;

    @GetMapping
    public String showFindPasswordPage(Model model) {
        model.addAttribute("userType", "GUEST");
        return "auth/find-password";
    }

    @PostMapping("/user")
    public String findUserPassword(@ModelAttribute FindUserPasswordDto dto, Model model) {
        boolean result = findPasswordService.verifyUser(dto);
        if (result) {
            return "redirect:/auth/reset-password?email=" + dto.getEmail() + "&userType=user";
        } else {
            model.addAttribute("userType", "GUEST");
            model.addAttribute("error", "비밀번호 재설정을 위한 정보를 찾을 수 없습니다.");
            return "auth/find-password";
        }
    }

    @PostMapping("/company")
    public String findCompanyPassword(@ModelAttribute FindCompanyPasswordDto dto, Model model) {
        boolean result = findPasswordService.verifyCompany(dto);
        if (result) {
            return "redirect:/auth/reset-password?email=" + dto.getEmail() + "&userType=company";
        } else {
            model.addAttribute("userType", "GUEST");
            model.addAttribute("error", "비밀번호 재설정을 위한 기업 정보를 찾을 수 없습니다.");
            return "auth/find-password";
        }
    }
}