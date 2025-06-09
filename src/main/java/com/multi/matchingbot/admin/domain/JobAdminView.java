package com.multi.matchingbot.admin.domain;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobAdminView extends BaseAuditDto {

    private Long id;
    private String formattedId;
    private String title;
    private String companyName;
    private String occupationName;
    private String address;
    private String skillKeywords;
    private String traitKeywords;
    private String enrollEmail;
    private LocalDate startDate;
    private LocalDate endDate;

}
