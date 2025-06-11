package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.mapper.CompanyAdminMapper;
import com.multi.matchingbot.admin.repository.CompanyAdminRepository;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateReportDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyAdminService {

    private final CompanyAdminRepository companyAdminRepository;
    private final CompanyAdminMapper companyAdminMapper;

    @Transactional
    public void deactivate(Long id) {
        Company company = companyAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (company.getStatus() == Yn.N) return;
        company.setStatus(Yn.N);
    }

    @Transactional
    public void reactivate(Long id) {
        Company company = companyAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        if (company.getStatus() == Yn.Y) return;
        company.setStatus(Yn.Y);
    }

    @Transactional
    public BulkResponseDto deactivateBulk(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                deactivate(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 비활성화 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }

    @Transactional
    public BulkResponseDto reactivateBulk(List<Long> ids) {
        List<Long> failed = new ArrayList<>();
        for (Long id : ids) {
            try {
                reactivate(id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 복구 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }

    public CompanyUpdateReportDto getReportSource(Long companyId) {
        Company company = companyAdminRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업이 존재하지 않습니다."));
        return companyAdminMapper.toReportDto(company);
    }
}
