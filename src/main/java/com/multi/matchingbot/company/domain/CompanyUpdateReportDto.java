package com.multi.matchingbot.company.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateReportDto {
    @NotBlank(message = "회사명을 입력하세요.")
    private String name;

    @Min(value = 1900, message = "설립연도는 1900년 이후여야 합니다.")
    @Max(value = 2100, message = "설립연도는 2100년 이전이어야 합니다.")
    private int yearFound;

    @Min(value = 1, message = "직원 수는 1명 이상이어야 합니다.")
    private int headcount;

    @NotBlank(message = "산업군을 입력하세요.")
    private String industry;

    @Min(value = 0, message = "연매출은 0 이상이어야 합니다.")
    private int annualRevenue;

    @Min(value = 0, message = "영업이익은 0 이상이어야 합니다.")
    private int operatingIncome;

    @Min(value = 0, message = "작년 채용 수는 0 이상이어야 합니다.")
    private int jobsLastYear;
}

