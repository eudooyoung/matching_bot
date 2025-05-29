package com.multi.matchingbot.company.service;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.repository.AuthCompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCompanyRegistService {

    private final AuthCompanyRepository authCompanyRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(CompanyRegisterDto dto) {
        if (authCompanyRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        if (dto.getAgreeTerms() != Yn.Y || dto.getAgreePrivacy() != Yn.Y || dto.getAgreeFinance() != Yn.Y) {
            throw new IllegalArgumentException("필수 약관에 모두 동의해야 합니다.");
        }

        Company company = Company.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .businessNo(dto.getBusinessNo())
                .address(dto.getAddress())
                .industry(dto.getIndustry())
                .yearFound(dto.getEstablishedYear())
                .headcount(dto.getHeadcount())
                .annualRevenue(dto.getAnnualRevenue())
                .operatingIncome(dto.getOperatingIncome())
                .jobsLastYear(dto.getJobsLastYear())
                .role(Role.COMPANY) // 필수 설정
                .agreeTerms(dto.getAgreeTerms())
                .agreePrivacy(dto.getAgreePrivacy())
                .agreeFinance(dto.getAgreeFinance())
                .agreeMarketing(dto.getAgreeMarketing())
                .agreeThirdParty(dto.getAgreeThirdParty())
                .status(Yn.Y)
                .build();

        authCompanyRepository.save(company);
    }
}