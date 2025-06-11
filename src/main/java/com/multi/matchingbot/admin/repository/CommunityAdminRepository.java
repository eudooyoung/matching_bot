package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.community.domain.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommunityAdminRepository extends JpaRepository<CommunityPost, Long> {

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT p FROM CommunityPost p
                LEFT JOIN p.member m
                LEFT JOIN p.company c
                LEFT JOIN p.category cat
                WHERE (:keyword IS NULL OR
                       p.title LIKE %:keyword% OR
                       m.nickname LIKE %:keyword% OR
                       c.name LIKE %:keyword% OR
                       CAST(p.id AS string) LIKE %:keyword%)
                  AND (:categoryId IS NULL OR cat.id = :categoryId)
                     AND (
                                :#{#writerType?.name()} IS NULL OR
                                (:#{#writerType?.name()} = 'MEMBER' AND p.member IS NOT NULL) OR
                                (:#{#writerType?.name()} = 'COMPANY' AND p.company IS NOT NULL)
                           )
            """)
    Page<CommunityPost> searchWithCondition(@Param("keyword") String keyword,
                                            @Param("categoryId") Long categoryId,
                                            @Param("writerType") Role writerType,
                                            Pageable pageable);
}
