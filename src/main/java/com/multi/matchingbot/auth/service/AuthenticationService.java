package com.multi.matchingbot.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String email, String password);                // loginRequest에서 이메일, 비번 받아옴 -> userDetails 반환

    String generateToken(UserDetails userdetails);                          // userDetails를 받아서 토큰 반환(현재는 accessToken)

    UserDetails validateToken(String token);                                // 토큰을 받아서
}
