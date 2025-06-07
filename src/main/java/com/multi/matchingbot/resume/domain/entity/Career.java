package com.multi.matchingbot.resume.domain.entity;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.resume.domain.CareerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "career")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Career extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CareerType careerType;

    @Column(nullable = false, length = 50)
    private String companyName;

    @Column(nullable = false, length = 50)
    private String departmentName;

    @Column(nullable = false, length = 50)
    private String positionTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int salary;

    @Column(nullable = false)
    private String careerSummary;


}
