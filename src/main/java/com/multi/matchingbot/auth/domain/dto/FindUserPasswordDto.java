package com.multi.matchingbot.auth.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserPasswordDto {
    private String email;
    private String name;
    private String phone1;
    private String phone2;
    private String phone3;

    public String getFullPhone() {
        return phone1 + "-" + phone2 + "-" + phone3;
    }
}