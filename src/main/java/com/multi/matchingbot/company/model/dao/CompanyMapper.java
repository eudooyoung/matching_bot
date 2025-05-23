package com.multi.matchingbot.company.model.dao;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyDto;
import org.springframework.beans.BeanUtils;

public class CompanyMapper {

    public static CompanyDto toDto(Company entity) {
        CompanyDto dto = new CompanyDto();


        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Company toEntity(CompanyDto dto) {
        Company entity = new Company();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
