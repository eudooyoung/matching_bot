package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JobAdminRepository extends JpaRepository<Job, Long> {

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
