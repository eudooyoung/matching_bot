package com.multi.matchingbot.member.domain.entities;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
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

    @Column(nullable = false)
    private String skillAnswer;

    @Column(nullable = false)
    private String traitAnswer;

    @Column(nullable = false, length = 100)
    private String skillKeywords;

    @Column(nullable = false, length = 100)
    private String talentKeywords;

}
