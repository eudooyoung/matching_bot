package com.multi.matchingbot.mapposting.repository;

import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapPostingRepository extends JpaRepository<Job, Long> {
}
