package com.multi.matchingbot.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MBotAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api") || "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // JSON 응답
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"접근 권한이 없습니다.\"}");
        } else {
            // 일반 페이지 접근 → 403 HTML 페이지로 redirect
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}

