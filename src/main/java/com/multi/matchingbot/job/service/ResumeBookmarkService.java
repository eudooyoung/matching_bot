package com.multi.matchingbot.job.service;

import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import org.springframework.stereotype.Service;

@Service
public class ResumeBookmarkService {

    private final ResumeBookmarkRepository resumeBookmarkRepository;

    public ResumeBookmarkService(ResumeBookmarkRepository resumeBookmarkRepository) {
        this.resumeBookmarkRepository = resumeBookmarkRepository;
    }

    public void deleteBookmark(Long companyId, Long resumeId) {
        resumeBookmarkRepository.deleteByCompanyIdAndResumeId(companyId, resumeId);
    }
}
