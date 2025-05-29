package com.multi.matchingbot.company.service;

import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.mapper.CompanyMapper;
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
}