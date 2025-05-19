package com.multi.matchingbot.auth;

import com.multi.matchingbot.auth.member.MemberDto;
import com.multi.matchingbot.auth.member.MemberMapper;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public MemberDto signup(MemberDto memberDto) {

    if (MemberMapper.findByEmail(memberDto.getMemberEmail()).isPresent()) {
        throw new DuplicateUsernameException("이메일이 중복됩니다.");
    }

    // 비밀번호 암호화
    memberDto.setMemberPassword(passwordEncoder.encode(memberDto.getMemberPassword()));

    // 역할 기본 설정
    if (memberDto.getMemberRole() == null) {
        memberDto.setMemberRole("ROLE_USER");
    }

    MemberMapper.insertMember(memberDto);

    return memberDto;
}