package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company extends BaseEntity {

    // 할일
    // validation 처리
    // default 값 처리
    // 테이블 연관 관계 처리

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeTerms;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreePrivacy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeFinance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeMarketing;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeThirdParty;

}
