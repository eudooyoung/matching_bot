package com.multi.matchingbot.auth.domain.dto;

import com.multi.matchingbot.common.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 64) // 상한만 제한
    private String password;

    private Role role;
}

