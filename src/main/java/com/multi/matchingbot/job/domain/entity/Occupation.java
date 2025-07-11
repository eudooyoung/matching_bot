package com.multi.matchingbot.job.domain.entity;

import com.multi.matchingbot.resume.domain.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "occupation")
@Builder
@Setter
@Getter
public class Occupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "occupation" , orphanRemoval = true)
    private List<Resume> resume = new ArrayList<>();

    @Column(nullable = false, unique = true, length = 15)
    private Long jobRoleCode;

    @Column(nullable = false, length = 15)
    private String jobRoleName;

    @Column(nullable = false, length = 15)
    private Long jobTypeCode;

    @Column(nullable = false, length = 15)
    private String jobTypeName;

    @Column(nullable = false, length = 15)
    private Long jobGroupCode;

    @Column(nullable = false, length = 15)
    private String jobGroupName;

}
