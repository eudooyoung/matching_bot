package com.multi.matchingbot.jobposting.repository;

import com.multi.matchingbot.jobposting.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
}
