package com.multi.matchingbot.job.service;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.job.domain.entity.ResumeBookmark;
import com.multi.matchingbot.job.repository.ResumeBookmarkRepository;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.repository.ResumeRepository;
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
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void addBookmark(Resume resumeId, Company companyId) {
        boolean exists = resumeBookmarkRepository.existsByResumeIdAndCompanyId(resumeId, companyId);
        if (exists) {
            // 이미 존재하는 즐겨찾기는 무시
            return;
        }

        Company company = companyRepository.findById(companyId.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 기업을 찾을 수 없습니다."));
        Resume resume = resumeRepository.findById(resumeId.getId())
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
}
