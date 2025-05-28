package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateDto {

    private Long id;
    private String email;
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
    private Yn agreeFinance;
    private Yn agreeMarketing;
    private Yn agreeThirdParty;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
