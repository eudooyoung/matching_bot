package com.multi.matchingbot.job.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeBookmarkDto {
    private Long companyId;
    private Long resumeId;
}