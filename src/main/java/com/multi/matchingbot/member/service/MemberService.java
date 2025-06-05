package com.multi.matchingbot.member.service;


import com.multi.matchingbot.member.domain.dtos.MemberUpdateDto;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.MemberRegisterDto;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.mapper.MemberMapper;
import com.multi.matchingbot.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public MemberUpdateDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
        return MemberMapper.toDto(member);
    }

    public MemberUpdateDto createMember(MemberUpdateDto dto) {
        Member member = MemberMapper.toEntity(dto);
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());
        return MemberMapper.toDto(memberRepository.save(member));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    /*public void update(MemberRegisterDto dto) {
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
    }*/

    /*@Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
    }*/

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    @Transactional(readOnly = true)
    public Long findIdByPrincipal(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("로그인된 사용자가 없습니다.");
        }
        return findByUsername(principal.getName()).getId();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 회사 정보를 찾을 수 없습니다: " + memberId));
    }

    public void update(MemberUpdateDto dto, Long memberId) {
        if (!dto.getId().equals(memberId)) {
            throw new IllegalArgumentException("회원 정보 수정 권한이 없습니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        member.setName(dto.getName());
        member.setAddress(dto.getAddress());
        member.setEmail(dto.getEmail());

        memberRepository.save(member);
    }

}