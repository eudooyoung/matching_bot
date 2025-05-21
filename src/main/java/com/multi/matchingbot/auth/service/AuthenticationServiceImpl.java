package com.multi.matchingbot.auth.service;


import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 8640000L;

    @Override
    public UserDetails authenticate(String email, String password) {

        /*
        로그 확인용

        try {
            Optional<Company> company = companyRepository.findByEmail(email);
            log.warn("회사 조회 성공: {}", company.isPresent());
        } catch (Exception e) {
            log.error("회사 조회 중 예외 발생", e);  // ← 이거로 실제 원인 로그 확인
        }
        */



        log.warn("AuthenticationService - authenticate 호출됨");

        Authentication authentication = authenticationManager.authenticate(                // authenticationManager가 authenticate메소드를 실행
                new UsernamePasswordAuthenticationToken(email, password)
        );

        log.warn("AuthenticationManager 통과 - principal: {}", authentication.getPrincipal());

        return userDetailsService.loadUserByUsername(email);



        /*
        로그 확인용

        log.warn("요청 password: [{}]", password);

        try {
            var authentication = authenticationManager.authenticate(                        // authenticationManager가 authenticate메소드를 실행
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
        */

    }


    @Override
    public String generateToken(UserDetails userdetails) {

        log.warn("AuthenticationService - generateToken 호출됨");

        try {

            MBotUserDetails mBotUserDetails = (MBotUserDetails) userdetails;

            Map<String, Object> claims = new HashMap<>();

            // 클레임 요소 추가
            claims.put("userType", mBotUserDetails.getUserType());
            claims.put("userId", mBotUserDetails.getId());

            log.warn("클레임 설정 완료: userType={}, userId={}", mBotUserDetails.getUserType(), mBotUserDetails.getId());

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userdetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))                                      // 발급 시간 설정
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))                      // 만료 시간 설정
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)                                    // 시그니쳐 알고리즘 특정
                    .compact();                                                                             // 스트링 반환

            log.warn("Access 토큰 생성 성공");

            System.out.println(token);

            return token;

        } catch (Exception ex) {
            log.error("Access 토큰 생성 중 예외 발생", ex);
            throw new RuntimeException("Access 토큰 생성 실패");
        }
    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);                                       // 토큰에서 username(email) 추출
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()                            // 검증 대상의 토큰에 있는 jwt키가 발 키와 같은지 검증하고, 같을 경우에만 username 반환
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);                        // JWT키 디코드
        log.warn("secretKey 길이 (bytes): {}", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
