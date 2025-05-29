package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.repository.ResumeAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeAdminService {

    private final ResumeAdminRepository resumeAdminRepository;

    public void deleteHard(Long id) {
        if (!resumeAdminRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 이력서가 존재하지 않습니다.");
        }
        resumeAdminRepository.deleteById(id);
    }

    public void deleteBulkHard(List<Long> checkedIds) {
        checkedIds.forEach(this::deleteHard);
    }
}
