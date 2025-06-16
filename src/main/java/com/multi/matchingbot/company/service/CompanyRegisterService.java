package com.multi.matchingbot.company.service;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import com.multi.matchingbot.company.repository.CompanyRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyRegisterService {

    private final CompanyRegisterRepository companyRegisterRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttachedItemService attachedItemService;

    /**
     * 기업 회원 가입 메소드
     *
     * @param dto 기업회원 로그인용 디티오
     */
    public void registerCompany(CompanyRegisterDto dto) {
        if (companyRegisterRepository.existsByEmail(dto.getEmail())) {
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
                .address(dto.getAddressRegion() +
                        (dto.getAddressDetail() != null && !dto.getAddressDetail().isBlank()
                                ? " " + dto.getAddressDetail()
                                : ""))
                .industry(dto.getIndustry())
                .yearFound(dto.getYearFound())
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

        log.debug(company.toString());

        Company savedCompany = companyRegisterRepository.save(company);

        /*기업 평가 보고서 로직*/
        attachedItemService.saveReportImageAsync(dto, savedCompany.getId());

    }
}
