package com.multi.matchingbot.auth.domain;

import com.multi.matchingbot.auth.domain.entities.RefreshToken;
import com.multi.matchingbot.common.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByEmailAndRole(String email, Role role);

    void deleteByRefreshToken(String refreshToken);
}
