package com.multi.matchingbot.calendar.repository;

import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarJobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEndDateBetween(LocalDate start, LocalDate end);
}
