package com.multi.matchingbot.admin.mapper;

import com.multi.matchingbot.admin.domain.view.ResumeAdminView;
import com.multi.matchingbot.resume.domain.dto.ResumeDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeAdminMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formattedId", expression = "java(ResumeAdminMapper.formattedId(resume.getId()))")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "skillKeywords", source = "skillKeywords")
    @Mapping(target = "traitKeywords", source = "traitKeywords")
//    @Mapping(target = "keywordsStatus", source = "keywordsStatus")
    @Mapping(target = "careerType", source = "careerType")
    @Mapping(target = "desiredOccupation", source = "occupation.jobRoleName")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ResumeAdminView toResumeAdminView(Resume resume);

    static String formattedId(Long id) {
        return String.format("R%05d", id);
    }

    public static ResumeDto toDto(Resume resume, boolean bookmarked) {
        ResumeDto.ResumeDtoBuilder builder = ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .traitKeywords(resume.getTraitKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .bookmarked(bookmarked);

        if (resume.getMember() != null) {
            builder.memberName(resume.getMember().getName());
        }

        return builder.build();
    }
}