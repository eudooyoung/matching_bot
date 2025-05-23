package com.multi.matchingbot.company.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false, unique = true)
    private Long businessNo;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 50)
    private String industry;

    @Column(nullable = false)
    private Integer yearFound;

    @Column(nullable = false)
    private Integer headcount;

    @Column(nullable = false)
    private Long annualRevenue;

    @Column(nullable = false)
    private Long operatingIncome;

    @Column(nullable = false)
    private Integer jobsLastYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo agreeTerms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo agreePrivacy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo agreeFinance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo agreeMarketing;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo agreeThirdParty;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String updatedBy;
    private LocalDateTime updatedAt;

    public enum Role {
        COMPANY
    }

    public enum YesNo {
        Y, N
    }
}