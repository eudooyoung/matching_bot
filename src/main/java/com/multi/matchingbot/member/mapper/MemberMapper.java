package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.dtos.MemberUpdateDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public interface MemberMapper {

    static MemberUpdateDto toDto(Member entity) {
        MemberUpdateDto dto = new MemberUpdateDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    static Member toEntity(MemberUpdateDto dto) {
        Member entity = new Member();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    static MemberUpdateDto toUpdateDto(Member member) {
        return toDto(member);
    }
}
