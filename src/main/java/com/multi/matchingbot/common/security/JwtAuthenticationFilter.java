package com.multi.matchingbot.common.security;

import com.multi.matchingbot.auth.TokenValidator;
import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.error.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MBotUserDetailsService mBotUserDetailsService;
    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 필터링 제외할 요청
    private static final String[] EXACT_PATHS = {
            "/health-check",
            "/favicon.ico"
    };

    private static final String[] WILDCARD_PATHS = {
            "/auth/**",
            "/swagger-ui/**",
            "/js/**",
            "/css/**",
            "/images/**",
            "/.well-known/**",
            "/error/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 바이패스 경로 처리
        if (shouldBypass(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 어세스토큰 추출
        String accessToken = extractToken(request, "accessToken");

        if (accessToken != null && tokenValidator.validateAccessToken(accessToken)) {
            // 어세스토큰 존재 & 유효 -> 인가 처리
            authenticateRequest(accessToken, request);
        } else {
            log.warn("!!! AccessToken 만료 >>> refreshToken 추출 시작");
            if (!handleRefreshFlow(request, response)) // re
                return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldBypass(String uri) {
        for (String path : EXACT_PATHS)
            if (uri.equals(path)) return true;

        for (String pattern : WILDCARD_PATHS)
            if (pathMatcher.match(pattern, uri)) return true;

        return false;
    }

    private String extractToken(HttpServletRequest request, String tokenName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (tokenName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean handleRefreshFlow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = extractToken(request, "refreshToken");
        if (refreshToken == null) {
            log.warn("RefreshToken 없음 -> 로그인 리다이렉트");
            response.sendRedirect(request.getRequestURI().startsWith("/admin") ? "/admin/login" : "/auth/login");
            return false;
        }

        try {
            tokenValidator.validateRefreshToken(refreshToken);
            log.warn("refreshToken 검증 완료 >>> 새로운 accessToken 발급 시작");

            String email = tokenProvider.extractUsername(refreshToken);
            String roleStr = tokenProvider.parseClaims(refreshToken).get("role", String.class);
            Role role = Role.valueOf(roleStr);

            UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);
            String newAccessToken = tokenProvider.generateAccessToken(userDetails);
            log.warn("*** 새로운 accessToken 발급 완료");

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(true)
                    .secure(false) // !!!!배포시 true로 전환!!!!!
                    .path("/")
                    .maxAge(tokenProvider.getAccessTokenExpireTime())
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;

        } catch (TokenException e) {
            log.warn("RefreshToken 발급 오류! 로그인 리다이렉션");
            String roleStr = tokenProvider.parseClaims(refreshToken).get("role", String.class);
            Role role = Role.valueOf(roleStr);
            response.sendRedirect(role == Role.ADMIN ? "/admin/login" : "/auth/login");
            return false;

        }
    }

    // 응답 인가 처리
    private void authenticateRequest(String token, HttpServletRequest request) {
        try {
            String email = tokenProvider.extractUsername(token);
            String roleStr = tokenProvider.parseClaims(token).get("role", String.class);
            Role role = Role.valueOf(roleStr);

            UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Principal: {}", auth.getPrincipal());
            log.info("Authorities: {}", auth.getAuthorities());

            if (userDetails instanceof MBotUserDetails mBotUserDetails) {
                request.setAttribute("userId", mBotUserDetails.getId());
                request.setAttribute("userType", mBotUserDetails.getRole());
                log.warn("JWT 인증 필터 통과 - 유저: {}, 권한: {}", userDetails.getUsername(), userDetails.getAuthorities());
            }
        } catch (Exception ex) {
            // Do not throw exceptions, just don't authenticate the user
            log.warn("JWT인증 실패");
        }
    }

}


