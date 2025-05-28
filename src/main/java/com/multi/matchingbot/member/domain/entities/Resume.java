package com.multi.matchingbot.member.domain.entities;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.jobposting.Occupation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(nullable = false)
    private Occupation occupation;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String skillAnswer;

    @Column
    private String traitAnswer;

    @Column(nullable = false, length = 100)
    private String skillKeywords;

    @Column(nullable = false, length = 100)
    private String talentKeywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    @Builder.Default
    private Yn keywordsStatus = Yn.N;

    @PreUpdate
    public void updateKeywordsStatus() {
        if (skillKeywords == null || talentKeywords == null) {
            this.keywordsStatus = Yn.N;
        } else {
            this.keywordsStatus = Yn.Y;
        }
    }
}
