package com.multi.matchingbot.company.model.dao;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyDto;
import org.springframework.beans.BeanUtils;

public class CompanyMapper {

    public static CompanyDto toDto(Company entity) {
        CompanyDto dto = new CompanyDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setBusinessNo(entity.getBusinessNo());
        dto.setAddress(entity.getAddress());
        dto.setIndustry(entity.getIndustry());
        dto.setYearFound(entity.getYearFound());
        dto.setHeadcount(entity.getHeadcount());
        dto.setAnnualRevenue(entity.getAnnualRevenue());
        dto.setOperatingIncome(entity.getOperatingIncome());
        dto.setJobsLastYear(entity.getJobsLastYear());
        dto.setAgreeTerms(entity.getAgreeTerms());
        dto.setAgreePrivacy(entity.getAgreePrivacy());
        dto.setAgreeFinance(entity.getAgreeFinance());
        dto.setAgreeMarketing(entity.getAgreeMarketing());
        dto.setAgreeThirdparty(entity.getAgreeThirdParty());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());

        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Company toEntity(CompanyDto dto) {
        Company entity = new Company();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
