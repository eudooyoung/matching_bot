package com.multi.matchingbot.jobposting.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDto {

    private Long id;
    private Long companyId;
    private Long occupationId;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "설명을 입력해주세요.")
    @Size(max = 500, message = "500자 이하로 입력해주세요.")
    private String description;

    @NotBlank(message = "주소를 입력해주세요.")
    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String address;

    @NotBlank(message = "주요 업무를 입력해주세요.")
    private String mainTask;

    @NotBlank(message = "필요 기술을 입력해주세요.")
    private String requiredSkills;

    @NotBlank(message = "필요 성향을 입력해주세요.")
    private String requiredTraits;

    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String skillKeywords;

    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String traitKeywords;

    @NotNull(message = "위도를 입력해주세요.")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private Double longitude;

    @NotNull(message = "시작일을 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "종료일을 입력해주세요.")
    private LocalDate endDate;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 50, message = "50자 이하로 입력해주세요.")
    private String enrollEmail;

    private String notice;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;
}
