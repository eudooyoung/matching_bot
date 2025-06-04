package com.multi.matchingbot.calendar.service;

import com.multi.matchingbot.calendar.domain.dto.CalendarDto;
import com.multi.matchingbot.calendar.repository.CalendarJobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarService {

    private final CalendarJobRepository calendarJobRepository;

    public CalendarService(CalendarJobRepository calendarJobRepository) {
        this.calendarJobRepository = calendarJobRepository;
    }

    public List<CalendarDto> getJobsByDeadline(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return calendarJobRepository.findJobsByEndDateBetween(start, end);
    }
}
