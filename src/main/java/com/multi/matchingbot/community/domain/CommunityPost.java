// CommunityPost.java
package com.multi.matchingbot.community.domain;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.domain.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CommunityPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CommunityCategory category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;


    @Size(max =100)
    private String title;

    @Size(max =500)
    private String content;
    private int views;
    /*private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;*/

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<CommunityComment> comments = new ArrayList<>();
}