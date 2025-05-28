package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.dtos.MemberAdminView;
import com.multi.matchingbot.member.domain.entities.Member;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formattedId", expression = "java(MemberMapper.formatId(member.getId()))")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "agreePrivacy", source = "agreePrivacy")
    @Mapping(target = "agreeService", source = "agreeService")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    MemberAdminView toMemberAdminView(Member member);

    static String formatId(Long id) {
        return String.format("U%05d", id);
    }
}
