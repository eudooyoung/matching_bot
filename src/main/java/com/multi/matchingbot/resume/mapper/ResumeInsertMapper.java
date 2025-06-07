package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.resume.domain.entity.Career;
import com.multi.matchingbot.resume.domain.dto.CareerDto;
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
    @Mapping(target = "skillAnswer", source = "strengthIntro")
    @Mapping(target = "traitAnswer", source = "personalityIntro")
    Resume toEntity(ResumeInsertDto dto);

    List<Career> toCareerEntities(List<CareerDto> dtoList);
}
