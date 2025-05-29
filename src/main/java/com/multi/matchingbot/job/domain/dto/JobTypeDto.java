package com.multi.matchingbot.job.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobTypeDto {
    private Long jobTypeCode;
    private String jobTypeName;
    private List<JobRoleDto> jobRoles;
}
