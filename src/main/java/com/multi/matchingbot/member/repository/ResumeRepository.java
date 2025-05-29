package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entities.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    @Query("""
                SELECT r FROM Resume r
                WHERE (:keyword IS NULL OR
                r.title LIKE %:keyword% OR
                r.createdBy LIKE %:keyword% OR
                CAST(r.id AS string) LIKE %:keyword%)
                AND (:keywordsStatus IS NULL OR r.keywordsStatus = :keywordsStatus)
            """)
    Page<Resume> searchWithCondition(@Param("keyword") String keyword, @Param("keywordsStatus") Yn keywordsStatus, Pageable pageable);
}
