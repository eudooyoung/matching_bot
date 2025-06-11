package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.resume.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public interface ResumeMapper {
    static ResumeDto toDto(Resume resume, boolean bookmarked) {

        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setId(resume.getId());
        resumeDto.setTitle(resume.getTitle());
        resumeDto.setSkillAnswer(resume.getSkillAnswer());
        resumeDto.setTraitAnswer(resume.getTraitAnswer());
        resumeDto.setSkillKeywords(resume.getSkillKeywords());
        resumeDto.setTraitKeywords(resume.getTraitKeywords());
        resumeDto.setSkillKeywordsConcat(resume.getSkillKeywords());
        resumeDto.setTraitKeywordsConcat(resume.getTraitKeywords());
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