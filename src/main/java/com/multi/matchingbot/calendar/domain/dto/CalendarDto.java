package com.multi.matchingbot.calendar.domain.dto;

import java.time.LocalDate;

public class CalendarDto
{
    private Long id;
    private String title;
    private LocalDate endDate;

    public CalendarDto(Long id, String title, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
