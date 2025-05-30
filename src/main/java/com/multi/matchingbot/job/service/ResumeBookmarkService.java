package com.multi.matchingbot.job.service;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.mapper.ResumeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeBookmarkService {

    private final ResumeBookmarkRepository resumeBookmarkRepository;

    public void toggleBookmark(Long companyId, Long resumeId) {
        Optional<ResumeBookmark> existing = resumeBookmarkRepository.findByCompanyIdAndResumeId(companyId, resumeId);
        if (existing.isPresent()) {
            resumeBookmarkRepository.delete(existing.get());
        } else {
            ResumeBookmark bookmark = new ResumeBookmark();

            Company company = new Company();
            company.setId(companyId);
            bookmark.setCompany(company);

            Resume resume = new Resume();
            resume.setId(resumeId);
            bookmark.setResume(resume);

            resumeBookmarkRepository.save(bookmark);
        }
    }

    public List<ResumeDto> getBookmarks(Long companyId) {
        List<ResumeBookmark> bookmarks = resumeBookmarkRepository.findWithResumeByCompanyId(companyId);
        return bookmarks.stream()
                .map(bookmark -> ResumeMapper.toDto(bookmark.getResume(), true))
                .collect(Collectors.toList());
    }

    public Page<ResumeDto> getBookmarkedResumePage(Long companyId, int page) {
        List<ResumeBookmark> bookmarks = resumeBookmarkRepository.findWithResumeByCompanyId(companyId);
        List<ResumeDto> result = bookmarks.stream()
                .map(bookmark -> ResumeMapper.toDto(bookmark.getResume(), true)) // ✅ 수정된 호출
                .collect(Collectors.toList());

        return new PageImpl<>(result, PageRequest.of(page, 10), result.size());
    }

    @Transactional
    public void deleteBookmarks(Long companyId, List<Long> resumeIds) {
        resumeBookmarkRepository.deleteByCompanyIdAndResumeIds(companyId, resumeIds);
    }

    @Transactional
    public void deleteBookmarks(List<Long> resumeIds, Long companyId) {
        resumeBookmarkRepository.deleteByCompanyIdAndResumeIds(companyId, resumeIds);
    }
}
