package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.career.domain.Career;
import com.multi.matchingbot.career.domain.CareerDetailDto;
import com.multi.matchingbot.resume.domain.dto.ResumeDetailDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ResumeDetailMapper {

    @Mapping(source = "member", target = "member")
    @Mapping(source = "occupation.id", target = "occupationId")
    @Mapping(target = "careers", expression = "java(toCareerDetailDtoList(resume.getCareers()))")
    ResumeDetailDto toDto(Resume resume);

    CareerDetailDto toCareerDetailDto(Career career);

    default List<CareerDetailDto> toCareerDetailDtoList(List<Career> careers) {
        if (careers == null) return null;
        return careers.stream()
                .map(this::toCareerDetailDto)
                .collect(Collectors.toList());
    }
}