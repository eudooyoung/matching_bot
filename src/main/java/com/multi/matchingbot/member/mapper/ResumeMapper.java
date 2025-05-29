package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Resume;

public class ResumeMapper {

    public static ResumeDto toDto(Resume resume, boolean bookmarked) {
        if (resume == null) return null;

        return ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .talentKeywords(resume.getTalentKeywords())
                .keywordsStatus(resume.getKeywordsStatus().name())
                .createdAt(resume.getCreatedAt())
                .memberName(resume.getMember() != null ? resume.getMember().getName() : null)
                .bookmarked(bookmarked)
                .build();
    }

    public static Resume toEntity(ResumeDto dto) {
        if (dto == null) return null;

        Resume resume = new Resume();
        resume.setId(dto.getId());
        resume.setTitle(dto.getTitle());
        resume.setSkillAnswer(dto.getSkillAnswer());
        resume.setTraitAnswer(dto.getTraitAnswer());
        resume.setSkillKeywords(dto.getSkillKeywords());
        resume.setTalentKeywords(dto.getTalentKeywords());
        resume.setKeywordsStatus(Yn.valueOf(dto.getKeywordsStatus()));
        resume.setCreatedAt(dto.getCreatedAt());
        // member, occupation은 필요 시 setter 추가 또는 외부에서 주입
        return resume;
    }
}