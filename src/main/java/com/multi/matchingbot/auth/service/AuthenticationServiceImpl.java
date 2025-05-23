package com.multi.matchingbot.auth.service;


import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.company.domain.CompanyTY;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CompanyRepository companyRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 8640000L;


    @Override
    public UserDetails authenticate(String email, String password) {

        try {
            Optional<CompanyTY> company = companyRepository.findByEmail(email);
            log.warn("회사 조회 성공: {}", company.isPresent());
        } catch (Exception e) {
            log.error("회사 조회 중 예외 발생", e);  // ← 이거로 실제 원인 로그 확인
        }

        log.warn("authenticate 확인 1");
        /*Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        // 인증 성공 여부 로그
        log.warn("✅ AuthenticationManager 통과 - principal: {}", authentication.getPrincipal());

        log.warn("authenticate 확인 2");


        return userDetailsService.loadUserByUsername(email);*/


        log.warn("요청 password: [{}]", password);

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            log.warn("AuthenticationManager 통과 - principal: {}", authentication.getPrincipal());

            var userDetails = (MBotUserDetails) authentication.getPrincipal();
            log.warn("권한: {}", userDetails.getAuthorities());

            log.warn("password.equals(): {}", password.equals(userDetails.getPassword()));

            return userDetails;
        } catch (Exception e) {
            log.error("인증 중 예외 발생", e);
            throw e;
        }
    }

    @Override
    public String generateToken(UserDetails userdetails) {

        log.warn("JWT 시작");
        try {


            MBotUserDetails matchingBotUserDetails = (MBotUserDetails) userdetails;
            log.warn("userDetails 캐스팅 성공: {}", matchingBotUserDetails.getUsername());

            Map<String, Object> claims = new HashMap<>();

            claims.put("userType", matchingBotUserDetails.getUserType());
            claims.put("userId", matchingBotUserDetails.getId());

            log.warn("클레임 설정 완료: userType={}, userId={}", matchingBotUserDetails.getUserType(), matchingBotUserDetails.getId());

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userdetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

            log.warn("JWT 생성 성공");

            return token;

        } catch (Exception ex) {
            log.error("JWT 생성 중 예외 발생", ex);
            throw new RuntimeException("JWT 생성 실패");
        }
    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        log.warn("secretKey 길이 (bytes): {}", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
