package com.multi.matchingbot.member.service;

import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void deleteAllByIds(List<Long> ids) {
        resumeRepository.deleteAllById(ids);
    }

    public List<Resume> findByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId);
    }

    public void updateResume(Long id, Resume updatedResume) {
        Resume resume = findById(id);
        resume.setTitle(updatedResume.getTitle());
        resume.setSkillAnswer(updatedResume.getSkillAnswer());
        resume.setTraitAnswer(updatedResume.getTraitAnswer());
        resumeRepository.save(resume);
    }

}
