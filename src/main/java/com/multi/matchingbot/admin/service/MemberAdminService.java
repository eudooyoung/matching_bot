package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.repository.MemberAdminRepository;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
       public void deactivateBulk(List<Long> checkedIds) {
           checkedIds.forEach(this::deactivate);
       }

       @Transactional
       public void reactivateBulk(List<Long> checkedIds) {
           checkedIds.forEach(this::reactivate);
       }
}
