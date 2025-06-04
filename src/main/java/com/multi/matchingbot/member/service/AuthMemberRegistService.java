package com.multi.matchingbot.member.service;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.MemberRegisterDto;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.repository.AuthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMemberRegistService {

    private final AuthMemberRepository authMemberRepository;
    private final PasswordEncoder passwordEncoder;

    private final AttachedItemService attachedItemService;

    public void register(MemberRegisterDto dto) {
        if (authMemberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        if (dto.getAgreeService() != Yn.Y || dto.getAgreePrivacy() != Yn.Y) {
            throw new IllegalArgumentException("필수 약관에 모두 동의해야 합니다.");
        }

        Member member = Member.builder()
                .role(Role.MEMBER)
                .name(dto.getName())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
//                .gender(dto.getGender())

                .agreeService(dto.getAgreeService())
                .agreePrivacy(dto.getAgreePrivacy())
                .agreeMarketing(dto.getAgreeMarketing())
                .agreeLocation(dto.getAgreeLocation())
                .alertBookmark(dto.getAlertBookmark())
                .alertResume(dto.getAlertResume())
                .status(Yn.Y)
                .build();

//        authMemberRepository.save(member);

        Member savedMember = authMemberRepository.save(member);

    }
}