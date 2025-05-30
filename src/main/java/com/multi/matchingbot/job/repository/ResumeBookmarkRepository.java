package com.multi.matchingbot.job.repository;

import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResumeBookmarkRepository extends JpaRepository<ResumeBookmark, Long> {

    @Transactional
    void deleteByCompanyIdAndResumeId(Long companyId, Long resumeId);
}

