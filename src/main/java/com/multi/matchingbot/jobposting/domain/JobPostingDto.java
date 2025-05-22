package com.multi.matchingbot.jobposting.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobPostingDto {

    private Long id;
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private LocalDateTime createdAt;
    private String requiredSkills;



    public static JobPostingDto fromEntity(JobPosting job) {
        JobPostingDto dto = new JobPostingDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setAddress(job.getAddress());
        dto.setLatitude(job.getLatitude() != null ? job.getLatitude() : 0.0);
        dto.setLongitude(job.getLongitude() != null ? job.getLongitude() : 0.0);
        dto.setCreatedAt(job.getCreatedAt());
        dto.setRequiredSkills(job.getRequiredSkills());

        return dto;
    }
}
