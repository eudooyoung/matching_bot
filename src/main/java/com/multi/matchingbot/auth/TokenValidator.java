package com.multi.matchingbot.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenValidator {

    private final TokenProvider tokenProvider;

    public boolean validateAccessToken(String token) {
        try {
            log.info("[TokenValidator] AccessToken 유효성 검사중: {}", token);
            // 토큰을 비밀 키 와함께 복호화를 진행 해서 유효하지 않으면 false 반환, 유효하면 true 반환
            Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.info("[TokenValidator] AccessToken 유효");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("[TokenValidator] 만료된 AccessToken. 토큰: {}, 만료 시각: {}", token, e.getClaims().getExpiration(), e);
            // accessToken 만료시 refreshToken 재발급 로직으로 돌림
            return false;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("[TokenValidator] 잘못된 AccessToken 서명. 토큰: {}", token, e);
            return false;

        } catch (UnsupportedJwtException e) {
            log.error("[TokenValidator] 지원되지 않는 AccessToken 토큰. 토큰: {}", token, e);
            return false;

        } catch (IllegalArgumentException e) {
            log.error("[TokenValidator] AccessToken 비어있거나 잘못됨. 토큰: {}", token, e);
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            log.info("[TokenValidator] RefreshToken 유효성 검사중: {}", token);
            // 토큰을 비밀 키 와함께 복호화를 진행 해서 유효하지 않으면 false 반환, 유효하면 true 반환
            Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.info("[TokenValidator] RefreshToken 유효");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("[TokenValidator] 만료된 RefreshToken. 토큰: {}, 만료 시각: {}", token, e.getClaims().getExpiration(), e);
            return false;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("[TokenValidator] 잘못된 RefreshToken 서명. 토큰: {}", token, e);
            return false;

        } catch (UnsupportedJwtException e) {
            log.error("[TokenValidator] 지원되지 않는 RefreshToken 토큰. 토큰: {}", token, e);
            return false;

        } catch (IllegalArgumentException e) {
            log.error("[TokenValidator] RefreshToken 비어있거나 잘못됨. 토큰: {}", token, e);
            return false;
        }
    }
}
