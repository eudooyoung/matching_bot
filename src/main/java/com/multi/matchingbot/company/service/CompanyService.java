package com.multi.matchingbot.company.service;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.mapper.CompanyMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {

    // private final, @RequiredArg로 의존성 주입 방식

    @Autowired
    private CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

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

    public CompanyUpdateDto findById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사입니다."));
        return companyMapper.toUpdateDto(company);
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

        companyRepository.save(company);
    }

    @Transactional
    public void deactivate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (company.getStatus() == Yn.N) return;
        company.setStatus(Yn.N);
    }

    @Transactional
    public void reactivate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (company.getStatus() == Yn.Y) return;
        company.setStatus(Yn.Y);
    }

    @Transactional
    public void deactivateBulk(List<Long> checkedIds) {
        checkedIds.forEach(this::deactivate);
    }

    @Transactional
    public void reactivateBulk(List<Long> checkedIds) {
        checkedIds.forEach(this::reactivate);
    }
}