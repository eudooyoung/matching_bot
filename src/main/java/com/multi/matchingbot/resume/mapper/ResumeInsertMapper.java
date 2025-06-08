package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.career.domain.CareerInsertDto;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResumeInsertMapper {
    @Mapping(target = "occupation.id", source = "occupationId")
    @Mapping(target = "member", ignore = true) // 나중에 주입
    @Mapping(target = "careers", source = "careers")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "skillAnswer", source = "skillAnswer")
    @Mapping(target = "traitAnswer", source = "traitAnswer")
    @Mapping(target = "skillKeywords", source = "skillKeywordsConcat")
    @Mapping(target = "traitKeywords", source = "traitKeywordsConcat")
    @Mapping(target = "careerType", source = "careerType")
    Resume toEntity(ResumeInsertDto dto);

//    List<Career> toCareerEntities(List<CareerInsertDto> dtoList);

    // 리스트용
    default List<Career> toCareerEntities(List<CareerInsertDto> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(this::toCareer)
                .toList();
    }

    // 커스텀 매핑: startDate, endDate는 DTO 내부의 변환 메서드 호출
    default Career toCareer(CareerInsertDto dto) {
        if (dto == null) return null;

        return Career.builder()
                .companyName(dto.getCompanyName())
                .departmentName(dto.getDepartmentName())
                .positionTitle(dto.getPositionTitle())
                .salary(dto.getSalary())
                .careerSummary(dto.getCareerSummary())
//                .careerType(dto.getCareerType())
                .startDate(dto.getStartDate())   // LocalDate ← "yyyy-MM" + "-01"
                .endDate(dto.getEndDate())
                .build();
    }
}
