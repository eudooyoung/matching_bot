package com.multi.matchingbot.member.domain.dtos;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import lombok.Data;

@Data
public class ResumeAdminViewDto extends BaseAuditDto {
    private Long id;
    private String formattedId;
    private String title;
    private String skillKeywords;
    private String talentKeywords;
    private String desiredOccupation;
}
