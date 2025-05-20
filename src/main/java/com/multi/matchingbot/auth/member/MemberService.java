package com.multi.matchingbot.auth.member;

import com.multi.matchingbot.auth.member.DAO.Member;
import com.multi.matchingbot.auth.member.DAO.MemberRepository;
import com.multi.matchingbot.auth.member.DTO.MemberRegisterDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setName(dto.getName());
        member.setBirthDate(LocalDate.of(dto.getYear(), dto.getMonth(), dto.getDay()));
        member.setGender(dto.getGender());
        member.setPhone(dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3());

        member.setTermsRequired(dto.isTermsRequired());
        member.setPrivacyRequired(dto.isPrivacyRequired());
        member.setLocationRequired(dto.isLocationRequired());
        member.setMarketingEmail(dto.isMarketingEmail());
        member.setMarketingSms(dto.isMarketingSms());

        memberRepository.save(member);
    }
}