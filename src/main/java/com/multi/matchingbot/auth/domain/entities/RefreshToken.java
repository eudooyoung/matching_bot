package com.multi.matchingbot.auth.domain.entities;

import com.multi.matchingbot.common.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public void update(String refreshToken, LocalDateTime issuedAt, LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }
}
