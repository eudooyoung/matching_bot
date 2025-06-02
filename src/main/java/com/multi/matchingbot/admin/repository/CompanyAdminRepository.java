package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CompanyAdminRepository extends JpaRepository<Company, Long> {
    @Query("""
                SELECT c FROM Company c
                WHERE 
                (:keyword IS NULL OR
                c.name LIKE %:keyword% OR
                c.email LIKE %:keyword% OR
                CAST(c.id AS string) LIKE %:keyword%)
                AND (:status IS NULL OR c.status = :status)
                AND (:reportStatus IS NULL OR c.reportStatus = :reportStatus)
            """)
        Page<Company> searchWithCondition(@Param("keyword") String keyword, @Param("status") Yn status, @Param("reportStatus") Yn reportStatus, Pageable pageable);

    long countByStatus(Yn yn);

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
