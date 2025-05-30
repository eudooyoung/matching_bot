package com.multi.matchingbot.auth.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCompanyPasswordDto {
    private String email;
    private String name;
    private String businessNo;
}