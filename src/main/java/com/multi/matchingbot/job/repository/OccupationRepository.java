package com.multi.matchingbot.job.repository;

import com.multi.matchingbot.job.domain.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {
    List<Occupation> findByJobRoleName(String jobRoleName);
}
