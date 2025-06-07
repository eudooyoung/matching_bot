package com.multi.matchingbot.job.service;

import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeBookmarkService {

    private final ResumeBookmarkRepository resumeBookmarkRepository;

    @Transactional
    public void deleteByResumeIdAndCompanyId(Long resumeId, Long companyId) {
        ResumeBookmark bookmark = resumeBookmarkRepository.findByResumeIdAndCompanyId(resumeId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 즐겨찾기 정보를 찾을 수 없습니다."));
        resumeBookmarkRepository.delete(bookmark);
    }

    @Transactional
    public void deleteAllByResumeIdsAndCompanyId(List<Long> resumeIds, Long companyId) {
        for (Long resumeId : resumeIds) {
            deleteByResumeIdAndCompanyId(resumeId, companyId);
        }
    }

    public Page<ResumeDto> getBookmarkedResumes(Long companyId, Pageable pageable) {
        return resumeBookmarkRepository.findResumeDtosByCompanyId(companyId, pageable);
    }
}
