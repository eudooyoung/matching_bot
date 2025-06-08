package com.multi.matchingbot.member.domain.dto;

import com.multi.matchingbot.common.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResumeDetailDto {
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String email;
    private String phone;
    private String address;

    public String getGenderLabel() {
        return gender.getLabel();
    }
}
