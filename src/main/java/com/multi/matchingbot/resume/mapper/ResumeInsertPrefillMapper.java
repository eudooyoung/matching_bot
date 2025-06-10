package com.multi.matchingbot.resume.mapper;

import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.dto.ResumeInsertDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
@Qualifier("resumeInsertPrefillMapper")
public interface ResumeInsertPrefillMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "birth", target = "birth")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    ResumeInsertDto toDto(Member member);

}
