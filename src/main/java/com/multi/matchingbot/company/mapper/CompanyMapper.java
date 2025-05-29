package com.multi.matchingbot.company.mapper;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public static CompanyUpdateDto toDto(Company entity) {
        CompanyUpdateDto dto = new CompanyUpdateDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Company toEntity(CompanyUpdateDto dto) {
        Company entity = new Company();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public CompanyUpdateDto toUpdateDto(Company company) {
        return toDto(company);
    }
}
