package com.multi.matchingbot.company;


import com.multi.matchingbot.company.domain.CompanyTY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyTY, Long> {

    Optional<CompanyTY> findByEmail(String email);

    boolean existsByEmail(String email);
}
