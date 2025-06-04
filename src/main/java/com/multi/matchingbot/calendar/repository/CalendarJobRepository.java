package com.multi.matchingbot.calendar.repository;

import com.multi.matchingbot.calendar.domain.dto.CalendarDto;
import com.multi.matchingbot.job.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CalendarJobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT new com.multi.matchingbot.calendar.domain.dto.CalendarDto(j.id, j.title, j.endDate) " +
            "FROM Job j " +
            "WHERE j.endDate BETWEEN :startDate AND :endDate")
    List<CalendarDto> findJobsByEndDateBetween(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}