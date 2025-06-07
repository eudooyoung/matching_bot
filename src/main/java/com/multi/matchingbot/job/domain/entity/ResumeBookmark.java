package com.multi.matchingbot.job.domain.entity;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.domain.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_bookmark",uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "resume_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

}

