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



    public void deleteHard(Long id) {
        if (!resumeRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 이력서가 존재하지 않습니다.");
        }

        resumeRepository.deleteById(id);
    }

    public void deleteAllByIds(List<Long> ids) {
        resumeRepository.deleteAllById(ids);
    }

    public void deleteBulkHard(List<Long> checkedIds) {
        checkedIds.forEach(this::deleteHard);
    }
}
