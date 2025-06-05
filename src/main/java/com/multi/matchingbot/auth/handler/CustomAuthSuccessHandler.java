package com.multi.matchingbot.auth.handler;

import com.multi.matchingbot.common.security.MBotUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        MBotUserDetails user = (MBotUserDetails) authentication.getPrincipal();
        String role = user.getRole().name();  // ✅ 수정: enum → String
        log.info("🎉 로그인 성공 - email: {}, role: {}", user.getEmail(), role);

        if ("COMPANY".equals(role)) {
            response.sendRedirect("/resumes");
        } else {
            response.sendRedirect("/main");
        }
    }
}
