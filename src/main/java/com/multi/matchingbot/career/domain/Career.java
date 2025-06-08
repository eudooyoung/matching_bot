package com.multi.matchingbot.career.domain;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.resume.domain.entity.Resume;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "career")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Career extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

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

    @Column(nullable = true)
    private Integer salary;

    @Column(nullable = true)
    private String careerSummary;

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}
