package com.multi.matchingbot.job.repository;

import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByCompany_Id(Long companyId, Pageable pageable);
    List<Job> findByEndDateBetween(LocalDate start, LocalDate end);

    @EntityGraph(attributePaths = {"company"})
    List<Job> findByCompany_Id(Long companyId);
}