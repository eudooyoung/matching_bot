package com.multi.matchingbot.auth.AuthCompany;

import com.multi.matchingbot.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);
}