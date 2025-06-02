package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.repository.ResumeAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public BulkResponseDto bulkHardDelete(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                deleteHard(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 비활성화 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }
}
