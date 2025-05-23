package com.multi.matchingbot.auth.AuthCompany;

import com.multi.matchingbot.company.domain.CompanyTY;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCompanyRepository extends JpaRepository<CompanyTY, Long> {
    boolean existsByEmail(String email);
}