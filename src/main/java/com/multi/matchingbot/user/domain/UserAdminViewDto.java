package com.multi.matchingbot.user.domain;

import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAdminViewDto {
    private Long id;
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
}
