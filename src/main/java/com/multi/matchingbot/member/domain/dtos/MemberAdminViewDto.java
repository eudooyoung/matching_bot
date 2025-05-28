package com.multi.matchingbot.member.domain.dtos;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

@Data
public class MemberAdminViewDto extends BaseAuditDto {
    private Long id;
    private String formattedId;
    private String name;
    private String email;
    private Gender gender;
    private String phone;
    private Yn status;
    private Yn agreePrivacy;
    private Yn agreeService;

    public String getGenderLabel() {
        return gender.getLabel();
    }

    public String getPrivacyLabel() {
        return agreePrivacy.name();
    }

    public String getServiceLabel() {
        return agreeService.name();
    }

    public String getStatusClass() {
        return status == Yn.Y ? "status-active" : "status-inactive";
    }

    public String getStatusText() {
        return status == Yn.Y ? "ACTIVE" : "INACTIVE";
    }


}
