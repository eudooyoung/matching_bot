package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyAdminService {

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
