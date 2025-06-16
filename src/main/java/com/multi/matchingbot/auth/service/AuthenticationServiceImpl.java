package com.multi.matchingbot.auth.service;


import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.auth.domain.RefreshTokenRepository;
import com.multi.matchingbot.auth.domain.entity.RefreshToken;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.error.TokenException;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.common.security.MBotUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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

    public UserDetails authenticate(String email, String password, Role role) {
        UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);

        if (!userDetails.isEnabled()) {
            throw new DisabledException("계정이 비활성화되어 있습니다. 관리자에게 문의하세요.");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return userDetails;
    }

//두영소스
//    @Override
//    @Transactional
//    public ResponseEntity<?> generateLoginResponse(UserDetails userDetails) {
//
//        log.warn("AuthenticationService - generateToken 호출됨");
//
//        MBotUserDetails mBotUserDetails = (MBotUserDetails) userDetails;
//        String email = mBotUserDetails.getUsername();
//        Role role = mBotUserDetails.getRole();
//        String accessToken = tokenProvider.generateAccessToken(userDetails);
//        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
//        LocalDateTime issuedAt = tokenProvider.getRefreshTokenIssuedDate();
//        LocalDateTime expiredAt = tokenProvider.getRefreshTokenExpireDate();
//
//
//        // refresh token 저장
//        RefreshToken tokenEntity = refreshTokenRepository.findByEmailAndRole(email, role)
//                .map(existing -> {
//                    existing.update(refreshToken, issuedAt, expiredAt);
//                    return existing;
//                })
//                .orElse(new RefreshToken(null, email, role, refreshToken, expiredAt, issuedAt));
//
//        refreshTokenRepository.save(tokenEntity);
//
//        log.warn("Access, Refresh 토큰 생성 성공");
//
//        //쿠키 생성
//        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
//                .httpOnly(true)
//                .secure(false) // !!!!배포시 true로 전환!!!!!
//                .path("/")
//                .maxAge(tokenProvider.getAccessTokenExpireTime())
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(false) // !!!!배포시 true로 전환!!!!!
//                .path("/")
//                .maxAge(tokenProvider.getRefreshTokenExpireTime())
//                .build();
//
//        log.warn("쿠키 생성 완료");
//
//        //쿠키 포함 응답 반환
//        return ResponseEntity.ok()
//                .header("Set-Cookie", accessCookie.toString())
//                .header("Set-Cookie", refreshCookie.toString())
//                .body("OK"); // 또는 return ResponseEntity.noContent().build();
//    }

    @Override
    @Transactional
    public ResponseEntity<?> generateLoginResponse(UserDetails userDetails) {

        log.warn("AuthenticationService - generateToken 호출됨");

        MBotUserDetails mBotUserDetails = (MBotUserDetails) userDetails;
        String email = mBotUserDetails.getUsername();
        Role role = mBotUserDetails.getRole();
        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        LocalDateTime issuedAt = tokenProvider.getRefreshTokenIssuedDate();
        LocalDateTime expiredAt = tokenProvider.getRefreshTokenExpireDate();

        // refresh token 저장
        RefreshToken tokenEntity = refreshTokenRepository.findByEmailAndRole(email, role)
                .map(existing -> {
                    existing.update(refreshToken, issuedAt, expiredAt);
                    return existing;
                })
                .orElse(new RefreshToken(null, email, role, refreshToken, expiredAt, issuedAt));
        refreshTokenRepository.save(tokenEntity);

        log.warn("Access, Refresh 토큰 생성 성공");

        // 쿠키 생성
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // TODO: https 배포 시 true로 전환
                .path("/")
                .maxAge(tokenProvider.getAccessTokenExpireTime())
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)  // TODO: https 배포 시 true로 전환
                .path("/")
                .maxAge(tokenProvider.getRefreshTokenExpireTime())
                .build();

        log.warn("쿠키 생성 완료");

        // ✅ role 기반 redirect 경로 설정
        String redirectUrl = switch (role) {
            case COMPANY -> "/resumes";
            case MEMBER -> "/main";
            case ADMIN -> "/admin";
            default -> "/main";
        };

        return ResponseEntity.ok()
                .header("Set-Cookie", accessCookie.toString())
                .header("Set-Cookie", refreshCookie.toString())
                .body(redirectUrl); // ✅ 프론트에서 window.location.href 로 사용
    }


    @Transactional
    @Override
    public void refreshTokenAndSetCookie(String refreshToken, HttpServletResponse response) {
        // 토큰에서 userId, userType 추출
        String email = tokenProvider.extractUsername(refreshToken);
        String roleStr = tokenProvider.parseClaims(refreshToken).get("role", String.class);
        Role role = Role.valueOf(roleStr);

        // DB조회
        RefreshToken savedToken = refreshTokenRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new TokenException("저장된 리프레시 토큰이 없습니다."));

        // 일치확인
        if (!savedToken.getRefreshToken().equals(refreshToken)) {
            throw new TokenException("리프레시 토큰이 일치하지 않습니다.");
        }

        // access 토큰 재발급
        UserDetails userDetails = mBotUserDetailsService.loadUserByTypeAndEmail(role, email);
        String newAccessToken = tokenProvider.generateAccessToken(userDetails);

        log.warn("access 토큰 재발급 완료");


        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false) // !!!!배포시 true로 전환!!!!!
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
