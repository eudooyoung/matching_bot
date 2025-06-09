package com.multi.matchingbot.resume.domain.entity;

import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id", nullable = false)
    private Occupation occupation;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 10)
//    private CareerType careerType;


    @Column(nullable = false, length = 50)
    private String title;

    @Column(name = "skill_answer", nullable = false, columnDefinition = "TEXT")
    private String skillAnswer;

    @Column(name = "trait_answer", nullable = false, columnDefinition = "TEXT")
    private String traitAnswer;

    @Column(name = "skill_keywords", length = 100)
    private String skillKeywords;

    @Column(name = "trait_keywords", length = 100)
    private String traitKeywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    @Builder.Default
    private Yn keywordsStatus = Yn.N;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CareerType careerType;

    public void updateFrom(Resume updatedResume) {
        this.title = updatedResume.getTitle();
        this.skillAnswer = updatedResume.getSkillAnswer();
        this.traitAnswer = updatedResume.getTraitAnswer();
        this.skillKeywords = updatedResume.getSkillKeywords();
        this.traitKeywords = updatedResume.getTraitKeywords();
    }

    @PreUpdate
    public void updateKeywordsStatus() {
        if (skillKeywords == null || traitKeywords == null) {
            this.keywordsStatus = Yn.N;
        } else {
            this.keywordsStatus = Yn.Y;
        }
    }

    // Resume.java
    public void updateBasicFields(ResumeUpdateDto dto) {
        this.title = dto.getTitle();
        this.careerType = dto.getCareerType();
        this.skillAnswer = dto.getSkillAnswer();
        this.traitAnswer = dto.getTraitAnswer();
        this.skillKeywords = dto.getSkillKeywordsConcat();
        this.traitKeywords = dto.getTraitKeywordsConcat();
    }

}