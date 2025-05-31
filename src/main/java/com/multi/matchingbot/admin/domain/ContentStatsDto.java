package com.multi.matchingbot.admin.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentStatsDto {
    // member
    private int memberToday, memberWeek, memberMonth;
    // company
    private int companyToday, companyWeek, companyMonth;
    // resume
    private int resumeToday, resumeWeek, resumeMonth;
    // job
    private int jobToday, jobWeek, jobMonth;
    // communityPost
    private int communityPostToday, communityPostWeek, communityPostMonth;
}
