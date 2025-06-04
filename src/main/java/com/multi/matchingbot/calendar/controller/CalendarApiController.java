package com.multi.matchingbot.calendar.controller;

import com.multi.matchingbot.calendar.domain.dto.CalendarDto;
import com.multi.matchingbot.calendar.service.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarApiController {

    private final CalendarService calendarService;

    public CalendarApiController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<CalendarDto> getJobsByMonth(@RequestParam(name = "year") int year, @RequestParam(name = "month") int month) {
        return calendarService.getJobsByDeadline(year, month);
    }
}
