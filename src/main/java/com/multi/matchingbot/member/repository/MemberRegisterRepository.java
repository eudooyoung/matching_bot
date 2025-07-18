package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRegisterRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Member> findByEmailAndName(String email, String name);

    Optional<Member> findByEmail(String email);

    boolean existsByNickname(String nickName);
}