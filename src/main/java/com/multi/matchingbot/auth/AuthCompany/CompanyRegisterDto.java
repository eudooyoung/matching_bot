package com.multi.matchingbot.auth.AuthCompany;

import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

@Data
public class CompanyRegisterDto {
    private String email;
    private String password;
    private String name;
    private String businessNo;
    private String address;
    private String industry;

    private int establishedYear;
    private int headcount;
    private int annualRevenue;
    private int operatingIncome;
    private int jobsLastYear;

    private Yn agreeTerms;
    private Yn agreePrivacy;
    private Yn agreeFinance;
    private Yn agreeMarketing;
    private Yn agreeThirdParty;
}