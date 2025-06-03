//package com.multi.matchingbot.mapposting.domain;
//
//import com.multi.matchingbot.common.domain.entity.BaseEntity;
//import com.multi.matchingbot.company.domain.Company;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "job") // ✅ 테이블 이름을 명시적으로 지정
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class MapPosting extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String title;
//
//    private String address;
//
//    @Column(name = "end_date")
//    private java.time.LocalDate endDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_id")
//    private Company company;
//
//    @Column
//    private Double latitude;
//
//    @Column
//    private Double longitude;
//
//    @Column(name = "required_skills")
//    private String requiredSkills;
//
//
//    // 필요한 필드는 여기에 추가
//}
