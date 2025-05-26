package com.multi.matchingbot.member.domain;

import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberAdminViewDto {
    private Long id;
    private String formattedId;
    private String name;
    private String email;
    private Gender gender;
    private String phone;
    private Yn status;
    private Yn agreePrivacy;
    private Yn agreeService;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

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
