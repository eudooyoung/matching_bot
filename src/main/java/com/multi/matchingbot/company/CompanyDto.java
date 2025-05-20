package com.multi.matchingbot.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private Long businessNo;
    private String address;
    private String industry;
    private int yearFound;
    private int headcount;
    private Long annualRevenue;
    private Long operatingIncome;
    private int jobsLastYear;

    private String agreeTerms;
    private String agreePrivacy;
    private String agreeFinance;
    private String agreeMarketing;
    private String agreeThirdparty;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
