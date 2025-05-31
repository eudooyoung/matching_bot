package com.multi.matchingbot.admin.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentStatsDto {
    // member
    private int memberToday, memberWeek, memberMonth, memberTotal;
    // company
    private int companyToday, companyWeek, companyMonth, companyTotal;
    // resume
    private int resumeToday, resumeWeek, resumeMonth, resumeTotal;
    // job
    private int jobToday, jobWeek, jobMonth, jobTotal;
    // communityPost
    private int communityPostToday, communityPostWeek, communityPostMonth, communityPostTotal;
}
