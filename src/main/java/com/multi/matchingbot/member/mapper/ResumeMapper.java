package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.dtos.ResumeAdminView;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Resume;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formattedId", expression = "java(ResumeMapper.formatId(resume.getId()))")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "skillKeywords", source = "skillKeywords")
    @Mapping(target = "talentKeywords", source = "talentKeywords")
    @Mapping(target = "keywordsStatus", source = "keywordsStatus")
    @Mapping(target = "desiredOccupation", source = "occupation.jobRoleName")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ResumeAdminView toResumeAdminView(Resume resume);

    static String formatId(Long id) {
            return String.format("R%05d", id);
    }

    public static ResumeDto toDto(Resume resume, boolean bookmarked) {
        ResumeDto.ResumeDtoBuilder builder = ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .talentKeywords(resume.getTalentKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .bookmarked(bookmarked);

        if (resume.getMember() != null) {
            builder.memberName(resume.getMember().getName());
        }

        return builder.build();
    }
}
