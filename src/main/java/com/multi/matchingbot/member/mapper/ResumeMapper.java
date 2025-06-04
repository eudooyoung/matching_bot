package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.entities.Resume;
import org.springframework.stereotype.Component;

@Component
public interface ResumeMapper {
    /*public static ResumeDto toDto(Resume resume, boolean bookmarked) {
        if (resume == null) return null;

        return ResumeDto.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .skillAnswer(resume.getSkillAnswer())
                .traitAnswer(resume.getTraitAnswer())
                .skillKeywords(resume.getSkillKeywords())
                .traitKeywords(resume.getTraitKeywords())
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
        resume.setTraitKeywords(dto.getTraitKeywords());
        resume.setKeywordsStatus(Yn.valueOf(dto.getKeywordsStatus()));
        resume.setCreatedAt(dto.getCreatedAt());
        // member, occupation은 필요 시 setter 추가 또는 외부에서 주입
        return resume;
    }*/
    static ResumeDto toDto(Resume resume, boolean bookmarked) {

        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setId(resume.getId());
        resumeDto.setTitle(resume.getTitle());
        resumeDto.setSkillAnswer(resume.getSkillAnswer());
        resumeDto.setTraitAnswer(resume.getTraitAnswer());
        resumeDto.setSkillKeywords(resume.getSkillKeywords());
        resumeDto.setTraitKeywords(resume.getTraitKeywords());
        resumeDto.setKeywordsStatus(String.valueOf(resume.getKeywordsStatus()));
        resumeDto.setCreatedAt(resume.getCreatedAt());
        resumeDto.setMemberName(resume.getMember() != null ? resume.getMember().getName() : null);
        resumeDto.setBookmarked(bookmarked);

        if (resume.getMember() != null) {
            resumeDto.setMemberId(resume.getMember().getId());
        }
        if (resume.getOccupation() != null) {
            resumeDto.setOccupationId(resume.getOccupation().getId());
        }

        return resumeDto;
    }

    static Resume toEntity(ResumeDto dto, Member member, Occupation occupation) {
        return Resume.builder()
                .member(member)
                .occupation(occupation)
                .title(dto.getTitle())
                .skillAnswer(dto.getSkillAnswer())
                .traitAnswer(dto.getTraitAnswer())
                .skillKeywords(dto.getSkillKeywords())
                .traitKeywords(dto.getTraitKeywords())
                .keywordsStatus(Yn.valueOf(dto.getKeywordsStatus()))
                .build();
    }
}