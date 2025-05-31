package com.multi.matchingbot.auth.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String email;
    private String newPassword;
    private String confirmPassword;
    private String userType;
}