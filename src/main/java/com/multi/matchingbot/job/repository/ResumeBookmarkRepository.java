package com.multi.matchingbot.job.repository;

import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.resume.domain.dto.ResumeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeBookmarkRepository extends JpaRepository<ResumeBookmark, Long> {
    @Query("SELECT rb FROM ResumeBookmark rb WHERE rb.resume.id = :resumeId AND rb.company.id = :companyId")
    Optional<ResumeBookmark> findByResumeIdAndCompanyId(@Param("resumeId") Long resumeId,
                                                        @Param("companyId") Long companyId);

    @Query("SELECT new com.multi.matchingbot.resume.domain.dto.ResumeDto(r.id, r.title, r.createdAt, m.name) " +
            "FROM ResumeBookmark b " +
            "JOIN b.resume r " +
            "JOIN r.member m " +
            "WHERE b.company.id = :companyId")
    Page<ResumeDto> findResumeDtosByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    boolean existsByResumeIdAndCompanyId(Long resumeId, Long companyId);

    Optional<ResumeBookmark> findByCompanyIdAndResumeId(Long companyId, Long resumeId);

    @Query("select rb.resume.id from ResumeBookmark rb where rb.company.id = :companyId")
    List<Long> findResumeIdsByCompanyId(@Param("companyId") Long companyId);
}