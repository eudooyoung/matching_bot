package com.multi.matchingbot.resume.domain.dto;

import com.multi.matchingbot.career.domain.CareerInsertDto;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.career.domain.CareerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResumeInsertDto {

    @NotBlank(message = "이력서 제목은 필수입니다.")
    @Size(max = 100, message = "이력서 제목은 100자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 30, message = "이름은 30자 이내여야 합니다.")
    private String name;

    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 100, message = "이메일은 100자 이내여야 합니다.")
    private String email;

    @NotBlank(message = "휴대폰 앞자리 선택은 필수입니다.")
    private String phone1;

    @NotBlank(message = "휴대폰 중간번호는 필수입니다.")
    @Pattern(regexp = "\\d{3,4}", message = "중간번호는 숫자 3~4자리여야 합니다.")
    private String phone2;

    @NotBlank(message = "휴대폰 끝번호는 필수입니다.")
    @Pattern(regexp = "\\d{4}", message = "끝번호는 숫자 4자리여야 합니다.")
    private String phone3;

    @Size(max = 255, message = "주소는 255자 이내여야 합니다.")
    private String address;

    @Size(max = 200, message = "기술 키워드는 200자 이내여야 합니다.")
    private String skillKeywordsConcat;

    @Size(max = 200, message = "성향 키워드는 200자 이내여야 합니다.")
    private String traitKeywordsConcat;

//    @NotNull(message = "경력 유형은 필수입니다.")
    private CareerType careerType;

    @NotNull(message = "직무는 필수입니다.")
    private Long occupationId;

    @Valid
    private List<CareerInsertDto> careers;

    @NotBlank(message = "기술 기반 자기소개서는 필수입니다.")
    @Size(max = 1000, message = "기술 기반 자기소개서는 1000자 이내여야 합니다.")
    private String skillAnswer;

    @NotBlank(message = "성향 기반 자기소개서는 필수입니다.")
    @Size(max = 1000, message = "성향 기반 자기소개서는 1000자 이내여야 합니다.")
    private String traitAnswer;

    private String phone; // 저장용


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
