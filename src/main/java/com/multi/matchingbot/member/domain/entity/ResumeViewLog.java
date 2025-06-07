package com.multi.matchingbot.member.domain.entity;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume-history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeViewLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 이력서를
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    // 어떤 기업이 열람했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;
}