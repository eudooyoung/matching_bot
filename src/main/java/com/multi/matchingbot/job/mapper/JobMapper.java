package com.multi.matchingbot.job.mapper;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Occupation;

import java.time.LocalDateTime;

public interface JobMapper {

    static JobDto toDto(Job entity) {

        JobDto dto = new JobDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAddress(entity.getAddress());
        dto.setMainTask(entity.getMainTask());
        dto.setRequiredSkills(entity.getRequiredSkills());
        dto.setRequiredTraits(entity.getRequiredTraits());
        dto.setSkillKeywords(entity.getSkillKeywords());
        dto.setTraitKeywords(entity.getTraitKeywords());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setEnrollEmail(entity.getEnrollEmail());
        dto.setNotice(entity.getNotice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());

        if (entity.getCompany() != null) {
            dto.setCompanyId(entity.getCompany().getId());
        }
        if (entity.getOccupation() != null) {
            dto.setOccupationId(entity.getOccupation().getId());
        }

        return dto;
    }

    static Job toEntity(JobDto dto, Company company, Occupation occupation) {
        return Job.builder()
                .company(company)
                .occupation(occupation)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .mainTask(dto.getMainTask())
                .requiredSkills(dto.getRequiredSkills())
                .requiredTraits(dto.getRequiredTraits())
                .skillKeywords(dto.getSkillKeywords())
                .traitKeywords(dto.getTraitKeywords())
                .latitude(dto.getLatitude() != null ? dto.getLatitude() : 37.5665)
                .longitude(dto.getLongitude() != null ? dto.getLongitude() : 126.9780)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .enrollEmail(dto.getEnrollEmail())
                .notice(dto.getNotice())
                .build();
    }

    static void updateFromDto(Job job, JobDto dto) {
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setAddress(dto.getAddress());
        job.setMainTask(dto.getMainTask());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setRequiredTraits(dto.getRequiredTraits());
        job.setSkillKeywords(dto.getSkillKeywords());
        job.setTraitKeywords(dto.getTraitKeywords());
        job.setStartDate(dto.getStartDate());
        job.setEndDate(dto.getEndDate());
        job.setEnrollEmail(dto.getEnrollEmail());
        job.setNotice(dto.getNotice());
        job.setLatitude(dto.getLatitude() != null ? dto.getLatitude() : 37.5665);
        job.setLongitude(dto.getLongitude() != null ? dto.getLongitude() : 126.9780);
        job.setUpdatedAt(LocalDateTime.now());
        job.setUpdatedBy("SYSTEM");
    }
}
