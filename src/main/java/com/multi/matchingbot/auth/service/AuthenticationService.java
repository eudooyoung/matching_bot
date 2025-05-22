package com.multi.matchingbot.auth.service;

import com.multi.matchingbot.auth.domain.dtos.TokenDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    UserDetails authenticate(String email, String password);                // loginRequest에서 이메일, 비번 받아옴 -> userDetails 반환

    TokenDto generateToken(UserDetails userdetails);                          // userDetails를 받아서 토큰 반환(현재는 accessToken)

    TokenDto refreshToken(String bearerToken);                          // userDetails를 받아서 토큰 반환(현재는 accessToken)

    void logout(String refreshToken);
}
