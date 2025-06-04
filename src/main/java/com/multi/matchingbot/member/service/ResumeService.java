package com.multi.matchingbot.member.service;

import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.dtos.ResumeViewLogDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.domain.entities.ResumeViewLog;
import com.multi.matchingbot.member.repository.ResumeRepository;
import com.multi.matchingbot.member.repository.ResumeViewLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeViewLogRepository resumeViewLogRepository;

    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 이력서를 찾을 수 없습니다."));
    }

    public Resume findByIdWithOccupation(Long id) {
        return resumeRepository.findWithOccupationById(id)
                .orElseThrow(() -> new EntityNotFoundException("이력서를 찾을 수 없습니다."));
    }

    public void deleteAllByIds(List<Long> ids) {
        resumeRepository.deleteAllById(ids);
    }

    public List<Resume> findByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId);
    }

    public Page<ResumeDto> getAllResumes(Pageable pageable) {
        return resumeRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Transactional
    public Resume save(Resume resume) {
        return resumeRepository.save(resume);
    }

    private ResumeDto toDto(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .traitKeywords(resume.getTraitKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .memberName(resume.getMember().getName())  // join 필요
                .bookmarked(false) // 모든 이력서 → 관심 여부는 false
                .build();
    }

    @Transactional
    public void update(Long id, Resume updatedResume) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("이력서를 찾을 수 없습니다."));

        resume.updateFrom(updatedResume); // 아래와 같이 정의 필요
    }

    /*public void updateResume(Long id, Resume updatedResume) {
        Resume resume = findById(id);
        resume.setTitle(updatedResume.getTitle());
        resume.setSkillAnswer(updatedResume.getSkillAnswer());
        resume.setTraitAnswer(updatedResume.getTraitAnswer());

        // occupation 수정
        if (updatedResume.getOccupation() != null) {
            resume.setOccupation(updatedResume.getOccupation());
        }

        resumeRepository.save(resume);
    }*/

    public List<ResumeViewLogDto> getResumeViewLogs(Long memberId) {
        List<ResumeViewLog> logs = resumeViewLogRepository.findByResume_Member_IdOrderByViewedAtDesc(memberId);

        return logs.stream().map(log -> ResumeViewLogDto.builder()
                .companyName(log.getCompany().getName())
                .industry(log.getCompany().getIndustry())
                .viewedAt(log.getViewedAt())
                .build()
        ).collect(Collectors.toList());
    }

}
