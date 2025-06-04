package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthMemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndName(String email, String name);

    Optional<Member> findByEmail(String email);
}