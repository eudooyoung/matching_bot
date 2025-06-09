package com.multi.matchingbot.resume.domain.dto;

import com.multi.matchingbot.career.domain.CareerDetailDto;
import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.member.domain.dto.MemberResumeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDetailDto {
    private Long id;
    private String title;

    private MemberResumeDetailDto member;
    private Long occupationId;

    private String skillAnswer;
    private String traitAnswer;
    private String skillKeywords;
    private String traitKeywords;
    private String keywordsStatus;
    private CareerType careerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CareerDetailDto> careers;
}