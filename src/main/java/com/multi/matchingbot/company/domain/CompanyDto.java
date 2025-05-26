package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    private String email;
    private String password;
    private Role role;

    private String name;
    private String phone;
    private String businessNo;
    private String address;
    private String industry;

    private int yearFound;
    private int headcount;
    private int annualRevenue;
    private int operatingIncome;
    private int jobsLastYear;

    private Yn agreeTerms;
    private Yn agreePrivacy;
    private Yn agreeProvide;
    private Yn agreeOpenApi;
    private Yn agreeMarketing;
    private Yn agreeThirdParty;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}