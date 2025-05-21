package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.auth.domain.AuthResponse;
import com.multi.matchingbot.auth.domain.LoginRequest;
import com.multi.matchingbot.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        log.warn("컨트롤러 진입");
        UserDetails userdetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        log.warn("jwt 생성 시도..");

        String tokenValue = authenticationService.generateToken(userdetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        log.warn("🎉 최종 응답 반환");

        return ResponseEntity.ok(authResponse);
    }
}
