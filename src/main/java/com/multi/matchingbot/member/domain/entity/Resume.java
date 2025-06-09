package com.multi.matchingbot.member.domain.entity;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.job.domain.entity.Occupation;
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

    @Column(name = "skill_answer", nullable = false, length = 255)
    private String skillAnswer;

    @Column(name = "trait_answer", nullable = false, length = 255)
    private String traitAnswer;

    @Column(name = "skill_keywords", length = 100)
    private String skillKeywords;

    @Column(name = "trait_keywords", length = 100)
    private String traitKeywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    @Builder.Default
    private Yn keywordsStatus = Yn.N;

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
}