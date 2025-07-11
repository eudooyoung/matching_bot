package com.multi.matchingbot.resume.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeViewLogDto {
    private String companyName;
    private String industry;
    private LocalDateTime viewedAt;
}