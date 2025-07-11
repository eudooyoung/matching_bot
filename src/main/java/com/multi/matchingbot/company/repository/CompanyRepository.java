package com.multi.matchingbot.company.repository;


import com.multi.matchingbot.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);

    Company getCompanyById(long referenceId);
}
