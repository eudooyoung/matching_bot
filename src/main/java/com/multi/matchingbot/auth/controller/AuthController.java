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
    public ResponseEntity<TokenDto> refresh(@RequestHeader("Authorization") String refreshToken) {
//        TokenDto tokenDto = authenticationService.generateToken(accessToken);
        return null;
    }
}
