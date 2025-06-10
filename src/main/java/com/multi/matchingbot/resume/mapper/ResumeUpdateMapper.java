package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.career.domain.CareerUpdateDto;
import com.multi.matchingbot.resume.domain.dto.ResumeUpdateDto;
import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResumeUpdateMapper {

    @Mapping(target = "occupation.id", source = "occupationId")
    @Mapping(target = "member", ignore = true) // 서비스에서 주입
    @Mapping(target = "careers", source = "careers")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "skillAnswer", source = "skillAnswer")
    @Mapping(target = "traitAnswer", source = "traitAnswer")
    @Mapping(target = "skillKeywords", source = "skillKeywordsConcat")
    @Mapping(target = "traitKeywords", source = "traitKeywordsConcat")
    Resume toEntity(ResumeUpdateDto dto);

    default List<Career> toCareerEntities(List<CareerUpdateDto> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(this::toCareer)
                .toList();
    }

    default Career toCareer(CareerUpdateDto dto) {
        if (dto == null) return null;

        return Career.builder()
                .id(dto.getId()) // ✅ 기존 경력 식별용
                .companyName(dto.getCompanyName())
                .departmentName(dto.getDepartmentName())
                .positionTitle(dto.getPositionTitle())
                .salary(dto.getSalary())
                .careerSummary(dto.getCareerSummary())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
