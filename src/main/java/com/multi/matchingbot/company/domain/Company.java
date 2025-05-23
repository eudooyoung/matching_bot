package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
public class Company extends BaseEntity {

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

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, unique = true)
    private String businessNo;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 50)
    private String industry;

    @Column(nullable = false)
    private int yearFound;

    @Column(nullable = false)
    private int headcount;

    @Column(nullable = false)
    private int annualRevenue;

    @Column(nullable = false)
    private int operatingIncome;

    @Column(nullable = false)
    private int jobsLastYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeTerms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreePrivacy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeFinance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeMarketing;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeThirdParty;
}