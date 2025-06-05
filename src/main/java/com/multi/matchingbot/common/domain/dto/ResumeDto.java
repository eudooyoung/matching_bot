//package com.multi.matchingbot.member.domain.dtos;
//
//import com.multi.matchingbot.member.domain.entities.Resume;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//@Builder
//public class ResumeDto {
//
//    private Long id;
//    private String name;
//    private String introduction;
//    private String skills;
//
//    public static ResumeDto fromEntity(Resume resume) {
//        return ResumeDto.builder()
//                .id(resume.getId())
//                .title(resume.getTitle())
//                .skillAnswer(resume.getSkillAnswer())
//                .traitAnswer(resume.getTraitAnswer())
//                .skillKeywords(resume.getSkillKeywords())
//                .traitKeywords(resume.getTraitKeywords())
//                .keywordsStatus(resume.getKeywordsStatus().name())
//                .createdAt(resume.getCreatedAt())
//                .memberName(resume.getMember().getName())  // member join 필요
//                .bookmarked(false)
//                .build();
//    }
//}
//
