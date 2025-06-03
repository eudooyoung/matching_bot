package com.multi.matchingbot.mapposting.domain;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.entity.Job;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MapPostingDto {

    private Long id;
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private LocalDateTime createdAt;
    private String requiredSkills;
    private String companyName; // ✅ 회사명 추가

    public static MapPostingDto fromEntity(Job job) {
        MapPostingDto dto = new MapPostingDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setAddress(job.getAddress());
        dto.setLatitude(job.getLatitude() != null ? job.getLatitude() : 0.0);
        dto.setLongitude(job.getLongitude() != null ? job.getLongitude() : 0.0);
        dto.setCreatedAt(job.getCreatedAt());
        dto.setRequiredSkills(job.getRequiredSkills());
        Company company = job.getCompany();
        dto.setCompanyName(company != null ? company.getName() : ""); // ✅ null 방지

        return dto;
    }
}
