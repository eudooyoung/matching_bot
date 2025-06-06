package com.multi.matchingbot.company.repository;

import com.multi.matchingbot.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRegisterRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);

    Optional<Company> findByEmailAndNameAndBusinessNo(String email, String name, String businessNo);

    Optional<Company> findByEmail(String email);
}