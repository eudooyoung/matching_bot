package com.multi.matchingbot.job.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobRoleDto {
    private Long id;             // Occupation PK
    private Long jobRoleCode;
    private String jobRoleName;
}
