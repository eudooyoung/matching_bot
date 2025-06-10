package com.multi.matchingbot.admin.domain.view;

import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import lombok.Data;

@Data
public class ResumeAdminView extends BaseAuditDto {
    private Long id;
    private String formattedId;
    private String title;
    private String skillKeywords;
    private String traitKeywords;
    private String desiredOccupation;
    private CareerType careerType;
//    private Yn keywordsStatus;
}
