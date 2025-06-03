package com.multi.matchingbot.member.service;

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
public class ResumeService {

    private final ResumeRepository resumeRepository;

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
                .talentKeywords(resume.getTalentKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .memberName(resume.getMember().getName())  // join 필요
                .bookmarked(false) // 모든 이력서 → 관심 여부는 false
                .build();
    }

    public void updateResume(Long id, Resume updatedResume) {
        Resume resume = findById(id);
        resume.setTitle(updatedResume.getTitle());
        resume.setSkillAnswer(updatedResume.getSkillAnswer());
        resume.setTraitAnswer(updatedResume.getTraitAnswer());
        resumeRepository.save(resume);
    }

}
