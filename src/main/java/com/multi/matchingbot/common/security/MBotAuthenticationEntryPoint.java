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

        // ✅ 보호된 경로가 아니라면 → 그냥 404 던짐
        if (!uri.startsWith("/admin") &&
                !uri.startsWith("/company") &&
                !uri.startsWith("/user")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (uri.startsWith("/admin")) {
            response.sendRedirect("/admin/login");
            return;
        }

        log.warn("인증 실패: 인증되지 않은 사용자가 {} 경로에 접근하려고 했습니다. 401 Unauthorized 반환.", request.getRequestURI());
        // 보호된 경로인데 로그인 안 되어 있음 → 로그인 페이지로 리다이렉트
        response.sendRedirect("/auth/login");
    }
}

