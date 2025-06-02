package com.multi.matchingbot.auth;

import com.multi.matchingbot.common.security.MBotUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 3;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14;


    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String generateToken(UserDetails userDetails, long expiresIn) {

        MBotUserDetails mBotUserDetails = (MBotUserDetails) userDetails;

        Map<String, Object> claims = new HashMap<>();

        // 클레임 요소 추가
        claims.put("role", mBotUserDetails.getRole().name());
        claims.put("userId", mBotUserDetails.getId());
        claims.put("email", mBotUserDetails.getEmail());


        log.warn("클레임 설정 완료: role={}, userId={}, userAuth={}",
                mBotUserDetails.getRole(), mBotUserDetails.getId(), mBotUserDetails.getAuthorities());

        return Jwts.builder()
                .setIssuer(issuer)
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))                                      // 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))                        //  만료 시간 설정
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)                                    // 시그니쳐 알고리즘 특정
                .compact();
    }

    public Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);                        // JWT키 디코드
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseClaims(String token) {
        try {
//            log.warn("클레임 파싱 시작");
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {           // 만료되어도 정보를 꺼내서 던짐
            return e.getClaims();
        }
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

    public long getAccessTokenExpireTime() {
        return ACCESS_TOKEN_EXPIRE_TIME / 1000;
    }

    public long getRefreshTokenExpireTime() {
        return REFRESH_TOKEN_EXPIRE_TIME / 1000;
    }

    public LocalDateTime getRefreshTokenExpireDate() {
        return LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRE_TIME / 1000);
    }

    public LocalDateTime getRefreshTokenIssuedDate() {
        return LocalDateTime.now();
    }
}
