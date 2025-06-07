package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MemberAdminRepository extends JpaRepository<Member, Long> {
    @Query("""
                SELECT m FROM Member m
                WHERE m.role = 'MEMBER'
                AND (:keyword IS NULL OR
                m.name LIKE %:keyword% OR
                m.email LIKE %:keyword% OR
                CAST(m.id AS string) LIKE %:keyword%)
                AND (:status IS NULL OR m.status = :status)
            """)
    Page<Member> searchWithCondition(@Param("keyword") String keyword, @Param("status") Yn status, Pageable pageable);

    long countByStatusAndRole(Yn status, Role role);

    Page<Member> findByRoleNot(Role role, Pageable pageable);

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);


}
