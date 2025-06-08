package com.multi.matchingbot.resume.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerDto {
    private String companyName;
    private String departmentName;
    private String positionTitle;
    private Integer salary;
    private String careerSummary;

    private String startDateRaw;
    private String endDateRaw;

    public LocalDate getStartDate() {
        return startDateRaw != null ? LocalDate.parse(startDateRaw + "-01") : null;
    }

    public LocalDate getEndDate() {
        return endDateRaw != null ? LocalDate.parse(endDateRaw + "-01") : null;
    }
}
