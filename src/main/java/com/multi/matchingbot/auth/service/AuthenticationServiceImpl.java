package com.multi.matchingbot.auth.service;


import com.multi.matchingbot.auth.TokenProvider;
import com.multi.matchingbot.auth.domain.RefreshTokenRepository;
import com.multi.matchingbot.auth.domain.dtos.TokenDto;
import com.multi.matchingbot.auth.domain.entities.RefreshToken;
import com.multi.matchingbot.common.error.TokenException;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.common.security.MBotUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MBotUserDetailsService mBotUserDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    public UserDetails authenticate(String email, String password) {

        log.warn("AuthenticationService - authenticate 호출됨");

        Authentication authentication = authenticationManager.authenticate(                // authenticationManager가 authenticate메소드를 실행
                new UsernamePasswordAuthenticationToken(email, password)
        );

        log.warn("AuthenticationManager 통과 - principal: {}", authentication.getPrincipal());

        return (UserDetails) authentication.getPrincipal();
    }


    @Override
    @Transactional
    public TokenDto generateToken(UserDetails userDetails) {

        log.warn("AuthenticationService - generateToken 호출됨");

        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        long expiresIn = tokenProvider.getAccessTokenExpireTime();

        MBotUserDetails mBotUserDetails = (MBotUserDetails) userDetails;
        String email = mBotUserDetails.getUsername();      // email 기준
        String userType = mBotUserDetails.getUserType();
        LocalDateTime issuedAt = tokenProvider.getRefreshTokenIssuedDate();
        LocalDateTime expiredAt = tokenProvider.getRefreshTokenExpireDate();

        RefreshToken tokenEntity = refreshTokenRepository.findByEmailAndUserType(email, userType)
                .map(existing -> {
                    existing.update(refreshToken, issuedAt, expiredAt);
                    return existing;
                })
                .orElse(new RefreshToken(null, email, userType, refreshToken, expiredAt, issuedAt));

        refreshTokenRepository.save(tokenEntity);

        log.warn("Access, Refresh 토큰 생성 성공");

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .build();
    }

    @Transactional
    @Override
    public TokenDto refreshToken(String bearerToken) {
        // 1. 토큰에서 userId, userType 추출
        String email = tokenProvider.extractUsername(bearerToken);
        String userType = tokenProvider.parseClaims(bearerToken).get("userType", String.class);

        // 2. DB에서 저장된 RefreshToken 조회
        RefreshToken savedToken = refreshTokenRepository.findByEmailAndUserType(email, userType)
                .orElseThrow(() -> new TokenException("저장된 리프레시 토큰이 없습니다."));

        // 3. 일치 여부 확인
        if (!savedToken.getRefreshToken().equals(bearerToken)) {
            throw new TokenException("리프레시 토큰이 일치하지 않습니다.");
        }

        // 4. UserDetails 로드 후 AccessToken 재발급
        UserDetails userDetails = mBotUserDetailsService.loadUserByUsername(email);

        String newAccessToken = tokenProvider.generateAccessToken(userDetails);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(bearerToken) // 기존 것 그대로
                .expiresIn(tokenProvider.getAccessTokenExpireTime())
                .build();
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

}
