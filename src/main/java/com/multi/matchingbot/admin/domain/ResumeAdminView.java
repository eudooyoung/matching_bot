package com.multi.matchingbot.admin.domain;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

@Data
public class ResumeAdminView extends BaseAuditDto {
    private Long id;
    private String formattedId;
    private String title;
    private String skillKeywords;
    private String talentKeywords;
    private String desiredOccupation;
    private Yn keywordsStatus;
}
