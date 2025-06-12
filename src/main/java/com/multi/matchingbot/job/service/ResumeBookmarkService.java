package com.multi.matchingbot.job.service;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import com.multi.matchingbot.resume.domain.dto.ResumeDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeBookmarkService {

    private final ResumeBookmarkRepository resumeBookmarkRepository;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void addBookmark(Long resumeId, Long companyId) {
        boolean exists = resumeBookmarkRepository.existsByResumeIdAndCompanyId(resumeId, companyId);
        if (exists) {
            // 이미 존재하는 즐겨찾기는 무시
            return;
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 기업을 찾을 수 없습니다."));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이력서를 찾을 수 없습니다."));

        ResumeBookmark bookmark = ResumeBookmark.builder()
                .company(company)
                .resume(resume)
                .build();

        resumeBookmarkRepository.save(bookmark);
    }

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

    /**
     * 즐겨찾기 토글: 존재하면 삭제, 없으면 추가
     * @return true → 즐겨찾기 추가됨, false → 즐겨찾기 해제됨
     */
    @Transactional
    public boolean toggleBookmark(Long companyId, Long resumeId) {
        Optional<ResumeBookmark> existing = resumeBookmarkRepository.findByCompanyIdAndResumeId(companyId, resumeId);

        if (existing.isPresent()) {
            resumeBookmarkRepository.delete(existing.get());
            return false; // 즐겨찾기 해제
        } else {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new EntityNotFoundException("회사 없음"));
            Resume resume = resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new EntityNotFoundException("이력서 없음"));

            ResumeBookmark bookmark = new ResumeBookmark(company, resume);
            resumeBookmarkRepository.save(bookmark);
            return true; // 즐겨찾기 추가
        }
    }

    public List<Long> getBookmarkedResumeIdsForCompany(Long companyId) {
        return resumeBookmarkRepository.findResumeIdsByCompanyId(companyId);
    }
}
