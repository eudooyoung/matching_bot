package com.multi.matchingbot.job.mapper;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.Job;
import com.multi.matchingbot.job.domain.JobDto;
import org.springframework.beans.BeanUtils;

public class JobMapper {

    public static JobDto toDto(Job entity) {

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
        if (entity.getOccupationId() != null) {
            dto.setOccupationId(entity.getOccupationId());
        }

        return dto;
    }

    public static Job toEntity(JobDto dto) {
        Job entity = new Job();
        BeanUtils.copyProperties(dto, entity);

        Company company = new Company();
        company.setId(dto.getCompanyId());
        entity.setCompany(company);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        return entity;
    }
}
