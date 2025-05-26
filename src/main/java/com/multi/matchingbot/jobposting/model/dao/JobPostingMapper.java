package com.multi.matchingbot.jobposting.model.dao;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.jobposting.domain.JobPosting;
import com.multi.matchingbot.jobposting.domain.JobPostingDto;
import org.springframework.beans.BeanUtils;

public class JobPostingMapper {

    public static JobPostingDto toDto(JobPosting entity) {

        System.out.println("🟡 [Mapper] entity.getStartDate() = " + entity.getStartDate());
        System.out.println("🟡 [Mapper] entity.getEndDate() = " + entity.getEndDate());

        JobPostingDto dto = new JobPostingDto();
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

        if (entity.getCompany() != null) {
            dto.setCompanyId(entity.getCompany().getId());
        }
        if (entity.getOccupationId() != null) {
            dto.setOccupationId(entity.getOccupationId());
        }

        return dto;
    }

    public static JobPosting toEntity(JobPostingDto dto) {
        JobPosting entity = new JobPosting();
        BeanUtils.copyProperties(dto, entity);

        Company company = new Company();
        company.setId(dto.getCompanyId());
        entity.setCompany(company);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        return entity;
    }
}
