package com.multi.matchingbot.auth.service;

import com.multi.matchingbot.company.repository.CompanyRegisterRepository;
import com.multi.matchingbot.auth.domain.dto.FindCompanyPasswordDto;
import com.multi.matchingbot.auth.domain.dto.FindUserPasswordDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindPasswordService {

    private final MemberRepository memberRepository;
    private final CompanyRegisterRepository companyRegisterRepository;

    public boolean verifyUser(FindUserPasswordDto dto) {
        Optional<Member> member = memberRepository.findByEmail(dto.getEmail());

        return member
                .filter(m -> m.getName().equals(dto.getName()))
                .filter(m -> m.getPhone().replaceAll("-", "")
                        .equals(dto.getFullPhone().replaceAll("-", ""))) // 하이픈 제거 비교
                .isPresent();
    }

    public boolean verifyCompany(FindCompanyPasswordDto dto) {
        return companyRegisterRepository
                .findByEmailAndNameAndBusinessNo(dto.getEmail(), dto.getName(), dto.getBusinessNo())
                .isPresent();
    }
}