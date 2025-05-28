package com.multi.matchingbot.member.service;

import com.multi.matchingbot.member.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public void delete(Long id) {
        if (!resumeRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 이력서가 존재하지 않습니다.");
        }

        resumeRepository.deleteById(id);
    }
}
