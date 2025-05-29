package com.multi.matchingbot.job.repository;

import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeBookmarkRepository extends JpaRepository<ResumeBookmark, Long> {

    Optional<ResumeBookmark> findByCompanyIdAndResumeId(Long companyId, Long resumeId);

    // N+1 쿼리 방지용 Fetch Join 추가
    @Query("SELECT b FROM ResumeBookmark b JOIN FETCH b.resume WHERE b.company.id = :companyId")
    List<ResumeBookmark> findWithResumeByCompanyId(@Param("companyId") Long companyId);
}