package com.multi.matchingbot.career.domain;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerInsertDto {

    @NotBlank(message = "회사명은 필수입니다.")
    @Size(max = 50, message = "회사명은 50자 이내여야 합니다.")
    private String companyName;

    @NotBlank(message = "부서명은 필수입니다.")
    @Size(max = 50, message = "부서명은 50자 이내여야 합니다.")
    private String departmentName;

    @NotBlank(message = "직급/직책은 필수입니다.")
    @Size(max = 50, message = "직급/직책은 50자 이내여야 합니다.")
    private String positionTitle;

    @NotBlank(message = "입사년월은 필수입니다.")
    private String startDateRaw;

    @NotBlank(message = "퇴사년월은 필수입니다.")
    private String endDateRaw;

    @Min(value = 0, message = "연봉은 0 이상이어야 합니다.")
    private int salary;

    @Size(max = 255, message = "담당 업무 요약은 255자 이내여야 합니다.")
    private String careerSummary;

    // 실제 매핑용
    public LocalDate getStartDate() {
        return LocalDate.parse(startDateRaw + "-01");
    }

    public LocalDate getEndDate() {
        return LocalDate.parse(endDateRaw + "-01");
    }
}

