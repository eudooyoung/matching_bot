package com.multi.matchingbot.auth.service;

import com.multi.matchingbot.company.repository.CompanyRegisterRepository;
import com.multi.matchingbot.auth.domain.dto.ResetPasswordDto;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final MemberRepository memberRepository;
    private final CompanyRegisterRepository companyRegisterRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean resetPassword(ResetPasswordDto dto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String encoded = passwordEncoder.encode(dto.getNewPassword());
            member.setPassword(encoded);
            memberRepository.save(member);
            return true;
        }

        return false;
    }

    public boolean resetCompanyPassword(ResetPasswordDto dto) {
        Optional<Company> optionalCompany = companyRegisterRepository.findByEmail(dto.getEmail());

        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            String encoded = passwordEncoder.encode(dto.getNewPassword());
            company.setPassword(encoded);
            companyRegisterRepository.save(company);
            return true;
        }

        return false;
    }
}