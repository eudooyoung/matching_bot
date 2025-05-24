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
        /*
        String uri = request.getRequestURI();

        if (uri.startsWith("/.well-known/")) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
            return;
        } else *//*if (uri.startsWith("/admin")) {
            response.sendRedirect("/admin/login");
            return;
        } else if (uri.startsWith("/")) {
            log.warn("니가 범인이지");
            response.sendRedirect("/auth/login");
            return;

        }



        // 401 Unauthorized 상태 코드와 JSON 메시지 직접 작성
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"인증되지 않은 사용자입니다. 로그인 후 다시 시도해 주세요.\"}");
        response.getWriter().flush();
        */

        String uri = request.getRequestURI();

        // ✅ 보호된 경로가 아니라면 → 그냥 404 던짐
        if (!uri.startsWith("/api")
                && !uri.startsWith("/admin")
                && !uri.startsWith("/company")
                && !uri.startsWith("/user")
                && !uri.startsWith("/member")
                && !uri.startsWith("/manager")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        log.warn("인증 실패: 인증되지 않은 사용자가 {} 경로에 접근하려고 했습니다. 401 Unauthorized 반환.", request.getRequestURI());
        // 보호된 경로인데 로그인 안 되어 있음 → 로그인 페이지로 리다이렉트
        response.sendRedirect("/auth/login");
    }
}

