package com.multi.matchingbot.job.mapper;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import org.springframework.stereotype.Component;

@Component
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
        dto.setSkillKeywordsConcat(entity.getSkillKeywords());
        dto.setTraitKeywordsConcat(entity.getTraitKeywords());
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
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .enrollEmail(dto.getEnrollEmail())
                .notice(dto.getNotice())
                .build();
    }
}
