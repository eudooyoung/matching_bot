package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.auth.domain.dtos.LoginRequest;
import com.multi.matchingbot.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        log.warn("컨트롤러 진입");
        log.warn("authenticate 시도...");

        UserDetails userdetails = authenticationService.authenticate(                   // authenticate 시도
                loginRequest.getEmail(),                                                // loginRequest에서 이메일과 비밀번호를 가져다 넘김
                loginRequest.getPassword(),
                loginRequest.getRole()
        );

        log.warn("쿠키 포함 응답 생성 시도...");
        return authenticationService.generateLoginResponse(userdetails);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        log.warn("리프레시 요청 수신 (쿠키 기반)");

        // 예외처리
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken 쿠키가 존재하지 않습니다.");
        }

        authenticationService.refreshTokenAndSetCookie(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken != null) {
            authenticationService.logout(refreshToken);
        }

        // accessToken, refreshToken 쿠키 제거
        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", clearAccess.toString());
        headers.add("Set-Cookie", clearRefresh.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }


}