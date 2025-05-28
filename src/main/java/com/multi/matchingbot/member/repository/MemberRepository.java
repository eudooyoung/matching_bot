package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Member> findByRoleNot(Role role, Pageable pageable);


    @Query("""
            SELECT m FROM Member m
            WHERE m.role <> 'ADMIN'
            AND (:keyword IS NULL OR
            m.name LIKE %:keyword% OR
            m.email LIKE %:keyword% OR
            CAST(m.id AS string) LIKE %:keyword%)
            AND (:status IS NULL OR m.status = :status)
        """)
    Page<Member> searchWithCondition(@Param("keyword") String keyword, @Param("status") Yn status, Pageable pageable);
}
