package com.multi.matchingbot.company.service;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.mapper.CompanyMapper;
import com.multi.matchingbot.company.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CompanyService {

    // private final, @RequiredArg로 의존성 주입 방식

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyUpdateDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회사 정보가 없습니다."));
        return CompanyMapper.toDto(company);
    }

    public CompanyUpdateDto createCompany(CompanyUpdateDto dto) {
        Company company = CompanyMapper.toEntity(dto);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        return CompanyMapper.toDto(companyRepository.save(company));
    }

    public CompanyUpdateDto updateCompany(Long id, CompanyUpdateDto dto) {
        Company existing = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회사 정보가 없습니다."));
        BeanUtils.copyProperties(dto, existing, "id", "createdAt", "createdBy");
        existing.setUpdatedAt(LocalDateTime.now());
        return CompanyMapper.toDto(companyRepository.save(existing));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public void update(CompanyUpdateDto dto, Long companyId) {
        if (!dto.getId().equals(companyId)) {
            throw new IllegalArgumentException("회사 정보 수정 권한이 없습니다.");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사입니다."));

        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        company.setPhone(dto.getPhone());
        company.setAddress(dto.getAddress());
        company.setIndustry(dto.getIndustry());
        company.setHeadcount(dto.getHeadcount());
        company.setAnnualRevenue(dto.getAnnualRevenue());
        company.setOperatingIncome(dto.getOperatingIncome());
        company.setJobsLastYear(dto.getJobsLastYear());

        companyRepository.save(company);
    }

    public Company findById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 회사 정보를 찾을 수 없습니다: " + companyId));
    }
}