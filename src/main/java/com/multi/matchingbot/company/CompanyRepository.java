package com.multi.matchingbot.company;


import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT c FROM Company c
            WHERE 
            (:keyword IS NULL OR
            c.name LIKE %:keyword% OR
            c.email LIKE %:keyword% OR
            CAST(c.id AS string) LIKE %:keyword%)
            AND (:status IS NULL OR c.status = :status)
        """)
    Page<Company> searchWithCondition(@Param("keyword") String keyword, @Param("status") Yn status, Pageable pageable);
}
