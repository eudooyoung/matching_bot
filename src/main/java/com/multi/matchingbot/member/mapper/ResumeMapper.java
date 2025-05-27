package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.dtos.ResumeAdminViewDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formattedId", expression = "java(formatId(resume.getId()))")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "skillKeywords", source = "skillKeywords")
    @Mapping(target = "talentKeywords", source = "talentKeywords")
    @Mapping(target = "desiredOccupation", source = "occupation.jobRoleName")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ResumeAdminViewDto toResumeAdminView(Resume resume);

    default String formatId(Long id) {
            return String.format("R%05d", id);
        }
}
