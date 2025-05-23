package com.multi.matchingbot.company.service;

import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyDto;
import com.multi.matchingbot.company.model.dao.CompanyMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회사 정보가 없습니다."));
        return CompanyMapper.toDto(company);
    }

    public CompanyDto createCompany(CompanyDto dto) {
        Company company = CompanyMapper.toEntity(dto);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        return CompanyMapper.toDto(companyRepository.save(company));
    }

    public CompanyDto updateCompany(Long id, CompanyDto dto) {
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