package com.multi.matchingbot.company.domain;

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
public class Company {

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
    private String name;

    @Column
    private String phone;

    @Column(nullable = false, unique = true)
    private Long businessNo;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private int yearFound;

    @Column(nullable = false)
    private int headCount;

    @Column(nullable = true)
    private int annualRevenue;

    @Column(nullable = true)
    private int operatingIncome;

    @Column(nullable = true)
    private int jobsLastYear;

    @Column(nullable = true)
    private Yn agreeTerms;

    @Column(nullable = true)
    private Yn agreePrivacy;

    @Column(nullable = true)
    private Yn agreeFinance;

    @Column(nullable = true)
    private Yn agreeMarketing;

    @Column(nullable = true)
    private Yn agreeThirdparty;

}
