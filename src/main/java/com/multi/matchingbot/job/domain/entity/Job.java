package com.multi.matchingbot.job.domain.entity;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
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
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id", nullable = false)
    private Occupation occupation;

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

    @Column(name = "skill_keywords", length = 200)
    private String skillKeywords;

    @Column(name = "trait_keywords", length = 200)
    private String traitKeywords;

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "enroll_email", nullable = false, length = 50)
    private String enrollEmail;

    @Column(length = 255)
    private String notice;

    public void updateFrom(Job updatedJob) {
        this.title = updatedJob.getTitle();
        this.description = updatedJob.getDescription();
        this.address = updatedJob.getAddress();
        this.mainTask = updatedJob.getMainTask();
        this.requiredSkills = updatedJob.getRequiredSkills();
        this.requiredTraits = updatedJob.getRequiredTraits();
        this.skillKeywords = updatedJob.getSkillKeywords();
        this.traitKeywords = updatedJob.getTraitKeywords();
        this.startDate = updatedJob.getStartDate();
        this.endDate = updatedJob.getEndDate();
        this.enrollEmail = updatedJob.getEnrollEmail();
        this.notice = updatedJob.getNotice();
        this.occupation = updatedJob.getOccupation(); // 직무 정보까지 복사
        this.latitude = updatedJob.getLatitude();
        this.longitude = updatedJob.getLongitude();
    }
}
