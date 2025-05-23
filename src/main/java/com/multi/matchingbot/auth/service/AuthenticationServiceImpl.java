package com.multi.matchingbot.auth.service;


import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.auth.domain.RefreshTokenRepository;
import com.multi.matchingbot.auth.domain.entities.RefreshToken;
import com.multi.matchingbot.common.error.TokenException;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.common.security.MBotUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MBotUserDetailsService mBotUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserDetails authenticate(String email, String password, String userType) {
        UserDetails userDetails = mBotUserDetailsService.loadByType(email, userType);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return userDetails;
    }


    @Override
    @Transactional
    public ResponseEntity<?> generateLoginResponse(UserDetails userDetails) {

        log.warn("AuthenticationService - generateToken 호출됨");

        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        MBotUserDetails mBotUserDetails = (MBotUserDetails) userDetails;
        String email = mBotUserDetails.getUsername();      // email 기준
        String userType = mBotUserDetails.getUserType();
        LocalDateTime issuedAt = tokenProvider.getRefreshTokenIssuedDate();
        LocalDateTime expiredAt = tokenProvider.getRefreshTokenExpireDate();


        // refresh token 저장
        RefreshToken tokenEntity = refreshTokenRepository.findByEmailAndUserType(email, userType)
                .map(existing -> {
                    existing.update(refreshToken, issuedAt, expiredAt);
                    return existing;
                })
                .orElse(new RefreshToken(null, email, userType, refreshToken, expiredAt, issuedAt));

        refreshTokenRepository.save(tokenEntity);

        log.warn("Access, Refresh 토큰 생성 성공");

        //쿠키 생성
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // HTTPS 시 true
                .path("/")
                .maxAge(tokenProvider.getAccessTokenExpireTime())
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(tokenProvider.getRefreshTokenExpireTime())
                .build();

        log.warn("쿠키 생성 완료");

        //쿠키 포함 응답 반환
        return ResponseEntity.ok()
                .header("Set-Cookie", accessCookie.toString())
                .header("Set-Cookie", refreshCookie.toString())
                .body("OK"); // 또는 return ResponseEntity.noContent().build();
    }

    @Transactional
    @Override
    public void refreshTokenAndSetCookie(String refreshToken, HttpServletResponse response) {
        // 토큰에서 userId, userType 추출
        String email = tokenProvider.extractUsername(refreshToken);
        String userType = tokenProvider.parseClaims(refreshToken).get("userType", String.class);

        // DB조회
        RefreshToken savedToken = refreshTokenRepository.findByEmailAndUserType(email, userType)
                .orElseThrow(() -> new TokenException("저장된 리프레시 토큰이 없습니다."));

        // 일치확인
        if (!savedToken.getRefreshToken().equals(refreshToken)) {
            throw new TokenException("리프레시 토큰이 일치하지 않습니다.");
        }

        // access 토큰 재발급
        UserDetails userDetails = mBotUserDetailsService.loadUserByUsername(email);
        String newAccessToken = tokenProvider.generateAccessToken(userDetails);


        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(tokenProvider.getAccessTokenExpireTime())
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }


}
