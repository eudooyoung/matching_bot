package com.multi.matchingbot.member.service;


import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.dtos.MemberRegisterDto;
import com.multi.matchingbot.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(MemberRegisterDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // ✅ 필수 Enum 값 누락 시 명확히 예외 처리
        if (dto.getGender() == null || dto.getGender().isBlank()) {
            throw new IllegalArgumentException("성별은 필수 입력입니다.");
        }


        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birth(LocalDate.of(dto.getYear(), dto.getMonth(), dto.getDay()))
                .gender(Gender.valueOf(dto.getGender())) // enum 처리
                .phone(dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3())
//                .agreeService(dto.isTermsRequired() ? Yn.Y : Yn.N)
//                .agreePrivacy(dto.isPrivacyRequired() ? Yn.Y : Yn.N)
                .agreeService(Yn.Y)
                .agreePrivacy(Yn.Y)
                .agreeLocation(dto.isLocationRequired() ? Yn.Y : Yn.N)
                .alertBookmark(dto.isMarketingEmail() ? Yn.Y : Yn.N)
                .alertResume(dto.isMarketingSms() ? Yn.Y : Yn.N)
                .status(Yn.Y)
                .role(Role.MEMBER)
                .address(dto.getAddress())
                .build();

        System.out.println(" 회원 등록 정보: " + member);  //  디버깅용 출력
        memberRepository.save(member);
    }

    @Transactional
    public void deactivate(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (member.getStatus() == Yn.N) return;
        member.setStatus(Yn.N);
    }

    @Transactional
    public void reactivate(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
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
    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }
}