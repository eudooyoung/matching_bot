package com.multi.matchingbot.auth.AuthCompany;

import lombok.Data;

@Data
public class CompanyRegisterDto {
    private String email;
    private String password;
    private String name;
    private String businessNo;
    private String address;
    private String industry;

    private Integer establishedYear;
    private Integer employeeCount;
    private Long annualSales;
    private Long operatingProfit;
    private Integer recentJobPosts;

    private boolean agreeTerms;
    private boolean agreePrivacy;
    private boolean agreeProvide;
    private boolean agreeOpenApi;

    private boolean agreeMarketing;
    private boolean agreeThirdParty;
}