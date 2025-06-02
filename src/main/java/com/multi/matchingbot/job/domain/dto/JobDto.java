package com.multi.matchingbot.job.domain.dto;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

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

    private Double latitude;

    private Double longitude;

    @NotNull(message = "시작일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    public Job toEntity() {
        Job job = new Job();
        job.setTitle(this.title);
        job.setDescription(this.description);
        job.setAddress(this.address);
        job.setMainTask(this.mainTask);
        job.setRequiredSkills(this.requiredSkills);
        job.setRequiredTraits(this.requiredTraits);
        job.setSkillKeywords(this.skillKeywords);
        job.setTraitKeywords(this.traitKeywords);
        job.setStartDate(this.startDate);
        job.setEndDate(this.endDate);
        job.setEnrollEmail(this.enrollEmail);
        job.setNotice(this.notice);
        job.setLatitude(this.latitude);
        job.setLongitude(this.longitude);
        return job;
    }

    public Job toEntityWithOccupation(Occupation occupation) {
        Job job = this.toEntity();
        job.setOccupation(occupation);
        return job;
    }
}
