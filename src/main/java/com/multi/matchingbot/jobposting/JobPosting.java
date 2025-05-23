package com.multi.matchingbot.jobposting;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
import com.multi.matchingbot.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "occupation_id", nullable = false)
    private Long occupationId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(name = "main_task", nullable = false, length = 255)
    private String mainTask;

    @Column(name = "required_skills", nullable = false, length = 255)
    private String requiredSkills;

    @Column(name = "required_traits", nullable = false, length = 255)
    private String requiredTraits;

    @Column(name = "skill_keywords", length = 100)
    private String skillKeywords;

    @Column(name = "trait_keywords", length = 100)
    private String traitKeywords;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "enroll_email", nullable = false, length = 50)
    private String enrollEmail;

    @Column(length = 255)
    private String notice;
}
