package com.multi.matchingbot.common.security;

import com.multi.matchingbot.auth.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = extractToken(request);

            if (token != null && tokenProvider.validateToken(token)) {

                String username = tokenProvider.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);                      // authenticationService의 validateToken 메소드 호출
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (userDetails instanceof MBotUserDetails mBotUserDetails) {
                    request.setAttribute("userId", mBotUserDetails.getId());
                    request.setAttribute("userType", mBotUserDetails.getUserType());

                    log.warn("JWT 인증 필터 통과 - 유저: {}, 권한: {}", userDetails.getUsername(), userDetails.getAuthorities());
                }
            }
        } catch (Exception ex) {
            // Do not throw exceptions, just don't authenticate the user
            log.warn("Received invalid auth token");
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");                // request에서 authroization에 해당하는 헤더만 가져옴
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
