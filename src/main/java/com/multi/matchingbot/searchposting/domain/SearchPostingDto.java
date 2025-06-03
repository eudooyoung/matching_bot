package com.multi.matchingbot.searchposting.domain;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPostingDto {
    private Long id;
    private String title;
    private String address;
    private String companyName;
    private String requiredSkills;

    // 추가: 직군/직종/직무
    private String jobGroupName;  // 직군
    private String jobTypeName;   // 직종
    private String jobRoleName;   // 직무

    public static SearchPostingDto fromEntity(Job job) {
        Occupation occupation = job.getOccupation();
        return SearchPostingDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .address(job.getAddress())
                .companyName(job.getCompany().getName())
                .requiredSkills(job.getRequiredSkills())
                .jobGroupName(occupation.getJobGroupName())
                .jobTypeName(occupation.getJobTypeName())
                .jobRoleName(occupation.getJobRoleName())
                .build();
    }
}

