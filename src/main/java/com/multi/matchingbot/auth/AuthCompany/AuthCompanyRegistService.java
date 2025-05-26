package com.multi.matchingbot.auth.AuthCompany;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
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

        Company company = Company.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .businessNo(dto.getBusinessNo())
                .address(dto.getAddress())
                .industry(dto.getIndustry())
                .yearFound(dto.getEstablishedYear())
                .headcount(dto.getEmployeeCount())
                .annualRevenue(dto.getAnnualSales() != null ? dto.getAnnualSales().intValue() : 0)
                .operatingIncome(dto.getOperatingProfit() != null ? dto.getOperatingProfit().intValue() : 0)
                .jobsLastYear(dto.getRecentJobPosts() != null ? dto.getRecentJobPosts() : 0)
                .role(Role.COMPANY) // 필수 설정
                .agreeTerms(dto.isAgreeTerms() ? Yn.Y : Yn.N)
                .agreePrivacy(dto.isAgreePrivacy() ? Yn.Y : Yn.N)
                /*.agreeProvide(dto.isAgreeProvide() ? Yn.Y : Yn.N)*/
                .agreeFinance(dto.isAgreeFinance() ? Yn.Y : Yn.N)
                .agreeMarketing(dto.isAgreeMarketing() ? Yn.Y : Yn.N)
                .agreeThirdParty(dto.isAgreeThirdParty() ? Yn.Y : Yn.N)
                .build();

        authCompanyRepository.save(company);
    }
}