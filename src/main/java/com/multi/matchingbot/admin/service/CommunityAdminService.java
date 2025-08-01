package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.repository.CommunityAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityAdminService {

    private final CommunityAdminRepository communityAdminRepository;

    public void deleteHard(Long id) {
        if (!communityAdminRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 공고가 존재하지 않습니다.");
        }
        communityAdminRepository.deleteById(id);
    }

    public BulkResponseDto bulkHardDelete(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                deleteHard(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 삭제 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }
}

/*public void deleteHard(Long id) {
    if (!jobAdminRepository.existsById(id)) {
        throw new EntityNotFoundException("해당 공고가 존재하지 않습니다.");
    }
    jobAdminRepository.deleteById(id);
}*/
