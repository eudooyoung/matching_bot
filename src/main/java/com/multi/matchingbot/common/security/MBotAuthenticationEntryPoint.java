package com.multi.matchingbot.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MBotAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        log.warn("인증 실패: {} 경로에 인증되지 않은 접근 시도", uri);

        if (uri.startsWith("/api")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (uri.startsWith("/admin")) {
            response.sendRedirect("/admin/login");
            return;
        }

        if (uri.startsWith("/member") || uri.startsWith("/company")) {
            response.sendRedirect("/auth/login");
            return;
        }

        // 기타 경로는 404
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

