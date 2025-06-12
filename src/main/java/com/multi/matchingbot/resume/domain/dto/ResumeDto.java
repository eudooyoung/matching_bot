package com.multi.matchingbot.resume.domain.dto;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.resume.domain.entity.Resume;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResumeDto {

    private Long id;                    // 이력서 ID
    private Long memberId;
    private Long occupationId;
    private String skillKeywordsConcat;      // 기술 키워드
    private String traitKeywordsConcat;     // 인재상 키워드
    private String keywordsStatus;     // 키워드 추출 상태 (Y/N)

    private LocalDateTime createdAt;   // 작성일
    private String memberName;         // 작성자 이름 (member 테이블 join 필요)
    private boolean bookmarked;        // 관심 이력서 여부 (join or 별도 체크)
    private Double similarityScore;
    
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "기술 기반 자기소개서를 입력해주세요.")
    @Size(min = 500, max = 1000, message = "500자 ~ 1000자 내로 입력해주세요.")
    private String skillAnswer;

    @NotBlank(message = "성향 기반 자기소개서를 입력해주세요.")
    @Size(min = 500, max = 1000, message = "500자 ~ 1000자 내로 입력해주세요.")
    private String traitAnswer;

    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String skillKeywords;

    @Size(max = 100, message = "100자 이하로 입력해주세요.")
    private String traitKeywords;

    public Resume toEntity(){
        Resume resume = new Resume();
        resume.setTitle(this.title);
        resume.setSkillAnswer(this.skillAnswer);
        resume.setTraitAnswer(this.traitAnswer);
        resume.setSkillKeywords(this.skillKeywords);
        resume.setTraitKeywords(this.traitKeywords);
        return resume;
    }
    public Resume toEntityWithOccupation(Occupation occupation) {
        Resume resume = this.toEntity();
        resume.setOccupation(occupation);
        return resume;
    }

    public ResumeDto(Long id, String title, LocalDateTime createdAt, String memberName) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.memberName = memberName;
    }
    public static ResumeDto fromEntity(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .traitKeywords(resume.getTraitKeywords())
                .keywordsStatus(resume.getKeywordsStatus() != null ? resume.getKeywordsStatus().name() : null)
                .createdAt(resume.getCreatedAt())
                .memberName(resume.getMember() != null ? resume.getMember().getName() : null)
                .bookmarked(false)
                .build();
    }

}