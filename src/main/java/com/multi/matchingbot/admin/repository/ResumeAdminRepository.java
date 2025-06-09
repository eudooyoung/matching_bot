package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ResumeAdminRepository extends JpaRepository<Resume, Long> {
    @Query("""
                SELECT r FROM Resume r
                WHERE (:keyword IS NULL OR
                       r.title LIKE %:keyword% OR
                       r.createdBy LIKE %:keyword% OR
                       CAST(r.id AS string) LIKE %:keyword%)
                       AND (:careerType IS NULL OR r.careerType = :careerType)
            """)
    Page<Resume> searchWithCondition(@Param("keyword") String keyword, @Param("careerType") CareerType careerType, Pageable pageable);

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}


// AND (:status IS NULL OR r.keywordsStatus = :status) 키워드 상태 추출용 쿼리
