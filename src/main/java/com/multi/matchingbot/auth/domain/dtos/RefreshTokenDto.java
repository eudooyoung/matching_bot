package com.multi.matchingbot.auth.domain.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshTokenDto {
    private Long id;
    private String email;
    private String refreshToken;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
}
