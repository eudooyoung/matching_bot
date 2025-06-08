package com.multi.matchingbot.resume.domain.dto;

import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.resume.domain.CareerType;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResumeInsertDto {
    private String title;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String email;
    private String phone;
    private String phone1;
    private String phone2;
    private String phone3;
    private String address;
    private String skillKeywordsConcat;
    private String traitKeywordsConcat;
    private CareerType careerType;

    // ✅ 직무
    private Long occupationId;

    // ✅ 경력 정보 여러 개
    @Valid
    private List<CareerDto> careers;

    // ✅ 자기소개서
    private String skillAnswer;
    private String traitAnswer;

    public void splitPhone(String fullPhone) {
        if (fullPhone != null && fullPhone.contains("-")) {
            String[] parts = fullPhone.split("-");
            this.phone1 = parts.length > 0 ? parts[0].trim() : "";
            this.phone2 = parts.length > 1 ? parts[1].trim() : "";
            this.phone3 = parts.length > 2 ? parts[2].trim() : "";
        }
    }

    public void mergePhone() {
        if (phone1 != null && phone2 != null && phone3 != null) {
            this.phone = String.join("-", phone1.trim(), phone2.trim(), phone3.trim());
        }
    }
}
