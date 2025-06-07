package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.repository.MemberAdminRepository;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;
    private final MemberAdminRepository memberAdminRepository;

    @Transactional
    public void deactivate(Long id) {
        Member member = memberAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (member.getStatus() == Yn.N) return;
        member.setStatus(Yn.N);
    }

    @Transactional
    public void reactivate(Long id) {
        Member member = memberAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (member.getStatus() == Yn.Y) return;
        member.setStatus(Yn.Y);
    }

    @Transactional
    public BulkResponseDto deactivateBulk(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                deactivate(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 비활성화 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }

    @Transactional
    public BulkResponseDto reactivateBulk(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                reactivate(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 복구 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }
}
