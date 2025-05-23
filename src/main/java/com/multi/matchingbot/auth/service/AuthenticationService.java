package com.multi.matchingbot.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String email, String password, String userType);                // loginRequest에서 이메일, 비번 받아옴 -> userDetails 반환

//    TokenDto generateToken(UserDetails userdetails);                          // userDetails를 받아서 토큰 반환(현재는 accessToken)

    ResponseEntity<?> generateLoginResponse(UserDetails userdetails);

    void refreshTokenAndSetCookie(String refreshToken, HttpServletResponse response);                          // userDetails를 받아서 토큰 반환(현재는 accessToken)

    void logout(String refreshToken);
}
