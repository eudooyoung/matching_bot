package com.multi.matchingbot.job.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeBookmarkDto {
    private Long id;
    private Long companyId;
    private Long resumeId;

    // 조회 시 편의 정보
    private String resumeTitle;
    private String memberName;
}