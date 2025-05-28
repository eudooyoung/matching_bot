package com.multi.matchingbot.auth.find;

import com.multi.matchingbot.auth.AuthCompany.AuthCompanyRepository;
import com.multi.matchingbot.auth.find.dto.FindCompanyPasswordDto;
import com.multi.matchingbot.auth.find.dto.FindUserPasswordDto;

import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindPasswordService {

    private final MemberRepository memberRepository;
    private final AuthCompanyRepository authCompanyRepository;

    public boolean verifyUser(FindUserPasswordDto dto) {
        Optional<Member> member = memberRepository.findByEmail(dto.getEmail());

        return member
                .filter(m -> m.getName().equals(dto.getName()))
                .filter(m -> m.getPhone().replaceAll("-", "")
                        .equals(dto.getFullPhone().replaceAll("-", ""))) // 하이픈 제거 비교
                .isPresent();
    }

    public boolean verifyCompany(FindCompanyPasswordDto dto) {
        return authCompanyRepository
                .findByEmailAndNameAndBusinessNo(dto.getEmail(), dto.getName(), dto.getBusinessNo())
                .isPresent();
    }
}