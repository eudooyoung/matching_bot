package com.multi.matchingbot.common.security;

import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.common.domain.enums.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
            "/.well-known/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        for (String exactPath : EXACT_PATHS) {
            if (requestURI.equals(exactPath)) {
                filterChain.doFilter(request, response);
//                log.info("[JwtFilter] 요청 URI가 제외 경로에 해당하여 필터를 건너뜁니다.");

                return;
            }
        }

        // 와일드카드 경로 매칭
        for (String wildcardPath : WILDCARD_PATHS) {
            if (pathMatcher.match(wildcardPath, requestURI)) {
//                log.info("[JwtFilter] 요청 URI가 와일드카드 제외 경로에 해당 → {}", wildcardPath);
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = extractToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            authenticateRequest(token, request);
        }

        filterChain.doFilter(request, response);
    }

    // 응답 인가 처리
    private void authenticateRequest(String token, HttpServletRequest request) {
        try {
            String email = tokenProvider.extractUsername(token);
            Role userType = tokenProvider.parseClaims(token).get("userType", Role.class);

            UserDetails userDetails = mBotUserDetailsService.loadUserByType(email, userType);

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

    // 권한 추출
    private List<GrantedAuthority> extractAuthoritiesFromToken(String token) {
        Claims claims = tokenProvider.parseClaims(token);

        List<?> rawRoles = claims.get("auth", List.class);
        List<String> roles = rawRoles.stream()
                .map(Object::toString)
                .toList();

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 토큰 추출
    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}


