package com.multi.matchingbot.common.advice; // 패키지 경로는 프로젝트 구조에 맞게 조정하세요

import com.multi.matchingbot.common.security.MBotUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    @ModelAttribute("role")
    public String addRoleToModel(@AuthenticationPrincipal MBotUserDetails user) {
        return (user != null) ? user.getRole().name() : "GUEST";
    }
}
