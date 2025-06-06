package com.multi.matchingbot.member.service;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.MemberRegisterDto;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.repository.AuthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        // ✅ 필수 Enum 값 누락 시 명확히 예외 처리
        if (dto.getGender() == null || dto.getGender().isBlank()) {
            throw new IllegalArgumentException("성별은 필수 입력입니다.");
        }

        if (authMemberRepository.existsByPhone(dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3())) {
            throw new IllegalArgumentException("이미 등록된 휴대폰 번호입니다.");
        }

        Member member = Member.builder()
                .role(Role.MEMBER)
                .name(dto.getName())
                .birth(LocalDate.of(dto.getYear(), dto.getMonth(), dto.getDay()))
                .gender(Gender.valueOf(dto.getGender())) // enum 처리
                .phone(dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3())
                .address(dto.getAddressRegion() +
                        (dto.getAddressDetail() != null && !dto.getAddressDetail().isBlank()
                                ? " " + dto.getAddressDetail()
                                : ""))
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .agreeService(Yn.Y)
                .agreePrivacy(Yn.Y)
                .agreeLocation(dto.isLocationRequired() ? Yn.Y : Yn.N)
                .agreeMarketing(dto.isMarketingEmail() ? Yn.Y : Yn.N)
//                .alertBookmark(Yn.Y)
//                .alertResume(Yn.Y)
                .status(Yn.Y)
                .build();


        Member savedMember = authMemberRepository.save(member);

    }
}