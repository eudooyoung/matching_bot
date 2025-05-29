package com.multi.matchingbot.job.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobGroupDto {
    private Long jobGroupCode;
    private String jobGroupName;
    private List<JobTypeDto> jobTypes;
}
