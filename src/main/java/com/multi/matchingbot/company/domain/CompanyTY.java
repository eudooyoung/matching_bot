package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanyTY extends BaseEntity{

    // 할일
    // validation 처리
    // default 값 처리
    // 테이블 연관 관계 처리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @Column(nullable = false, unique = true)
    private String businessNo;

    @Column(nullable = false)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeTerms;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreePrivacy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeProvide;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeOpenApi;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeMarketing;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeThirdParty;



}
