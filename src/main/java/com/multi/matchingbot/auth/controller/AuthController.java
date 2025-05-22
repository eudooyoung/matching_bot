package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.auth.domain.dtos.LoginRequest;
import com.multi.matchingbot.auth.domain.dtos.TokenDto;
import com.multi.matchingbot.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {

        log.warn("컨트롤러 진입");
        log.warn("authenticate 시도...");

        UserDetails userdetails = authenticationService.authenticate(                   // authenticate 시도
                loginRequest.getEmail(),                                                // loginRequest에서 이메일과 비밀번호를 가져다 넘김
                loginRequest.getPassword()
        );

        log.warn("jwt 생성 시도...");

        TokenDto tokenDto = authenticationService.generateToken(userdetails);           // authenticationServicedp authenticate된 userdetails를 넘겨서 토큰을 생성


        log.warn("*성공* 최종 응답 반환");

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestHeader("Authorization") String bearerToken) {

        log.warn("리프레시 요청 수신");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 올바르지 않습니다.");
        }

        TokenDto tokenDto = authenticationService.refreshToken(bearerToken.substring(7)); // Bearer 제거 후 위임
        log.warn("AccessToken 재발급 완료");

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 잘못되었습니다.");
        }
        String refreshToken = bearerToken.substring(7);
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }


}