package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.admin.domain.enums.EndStatus;
import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface JobAdminRepository extends JpaRepository<Job, Long> {

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT j FROM Job j
                LEFT JOIN j.company c
                LEFT JOIN j.occupation o
                WHERE (:keyword IS NULL OR
                       j.title LIKE %:keyword% OR
                       c.name LIKE %:keyword% OR
                       o.jobRoleName LIKE %:keyword% OR
                       CAST(j.id AS string) LIKE %:keyword%)
               AND (
                        :#{#endStatus.name()} = 'ALL' OR
                       ( :#{#endStatus.name()} = 'EXPIRED' AND j.endDate < :today) OR
                       ( :#{#endStatus.name()} = 'OPEN' AND j.endDate >= :today)
                   )
            """)
    Page<Job> searchWithCondition(@Param("keyword") String keyword,
                                  @Param("endStatus") EndStatus endstatus,
                                  @Param("today") LocalDate today,
                                  Pageable pageable);

}
