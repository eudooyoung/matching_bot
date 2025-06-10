package com.multi.matchingbot.career.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDetailDto {
    private String companyName;
    private String departmentName;
    private String positionTitle;
    private String careerType;
    private Integer salary;

    private LocalDate startDate;
    private LocalDate endDate;

    private String careerSummary;
}
