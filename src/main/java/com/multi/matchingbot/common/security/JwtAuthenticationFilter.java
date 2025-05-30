package com.multi.matchingbot.common.security;

import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.common.domain.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        for (String exactPath : EXACT_PATHS)
            if (requestURI.equals(exactPath)) {
                filterChain.doFilter(request, response);
                return;
            }

        // 와일드카드 경로 매칭
        for (String wildcardPath : WILDCARD_PATHS)
            if (pathMatcher.match(wildcardPath, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }

        // accessToken 재발급 로직
        String accessToken = extractAccessToken(request);

        if (accessToken != null) {
            try {
                if (tokenProvider.validateToken(accessToken)) {
                    authenticateRequest(accessToken, request);
                }
            } catch (ExpiredJwtException e) {
                log.warn("AccessToken 만료 >>> refreshToken 추출 시작");
                String refreshToken = extractRefreshToken(request);
                if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
                    log.warn("refreshToken 검증 완료 >>> 새로운 accessToken 발급 시작");
                    String email = tokenProvider.extractUsername(refreshToken);
                    String roleStr = tokenProvider.parseClaims(refreshToken).get("role", String.class);
                    Role role = Role.valueOf(roleStr);

                    UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);
                    String newAccessToken = tokenProvider.generateAccessToken(userDetails);
                    log.warn("새 accessToken 발급 완료");

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
                } else {
                    log.warn("RefreshToken 만료");
                    String roleStr = tokenProvider.parseClaims(refreshToken).get("role", String.class);
                    Role role = Role.valueOf(roleStr);

                    if (role == Role.ADMIN) {
                        response.sendRedirect("/admin/login");
                    } else {
                        response.sendRedirect("/auth/login");
                    }

                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // 응답 인가 처리
    private void authenticateRequest(String token, HttpServletRequest request) {
        try {
//            log.warn("인가 요청 진입");
            String email = tokenProvider.extractUsername(token);
//            log.warn("유저 이메일 복원: {}", email);
            String roleStr = tokenProvider.parseClaims(token).get("role", String.class);
            Role role = Role.valueOf(roleStr);
//            log.warn("유저 정보 복원 완료 email: {}, role: {}", email, role);

            UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);
//            log.warn("userDetail 생성");

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

    // 토큰 추출
    private String extractAccessToken(HttpServletRequest request) {
//        log.warn("토큰 추출 진입");
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        //        log.warn("토큰 추출 진입");
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}


