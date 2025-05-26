package com.multi.matchingbot.member;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Member> findByRoleNot(Role role, Pageable pageable);

//    void delete(Member member);

}
