package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.career.domain.CareerUpdateDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
@Qualifier("resumeUpdatePrefillMapper")
public interface ResumeUpdatePrefillMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "occupationId", source = "occupation.id")
    @Mapping(target = "skillKeywordsConcat", source = "skillKeywords")
    @Mapping(target = "traitKeywordsConcat", source = "traitKeywords")
    @Mapping(target = "careers", source = "careers")
    @Mapping(target = "name", source = "member.name")
    @Mapping(target = "birth", source = "member.birth")
    @Mapping(target = "gender", source = "member.gender")
    @Mapping(target = "email", source = "member.email")
    @Mapping(target = "phone", source = "member.phone")
    @Mapping(target = "address", source = "member.address")
    ResumeUpdateDto toDto(Resume resume);

    List<CareerUpdateDto> toCareerDtos(List<Career> careers);

    default CareerUpdateDto toCareerDto(Career career) {
        if (career == null) return null;

        CareerUpdateDto dto = new CareerUpdateDto();
        dto.setId(career.getId());
        dto.setCompanyName(career.getCompanyName());
        dto.setDepartmentName(career.getDepartmentName());
        dto.setPositionTitle(career.getPositionTitle());
        dto.setSalary(career.getSalary());
        dto.setCareerSummary(career.getCareerSummary());

        // LocalDate → yyyy-MM 변환
        dto.setStartDateRaw(formatYearMonth(career.getStartDate()));
        dto.setEndDateRaw(formatYearMonth(career.getEndDate()));

        return dto;
    }

    default String formatYearMonth(LocalDate date) {
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM")) : null;
    }
}
