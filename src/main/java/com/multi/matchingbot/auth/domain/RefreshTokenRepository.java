package com.multi.matchingbot.auth.domain;

import com.multi.matchingbot.auth.domain.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByEmailAndUserType(String email, String type);

    void deleteByRefreshToken(String refreshToken);
}
