package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.dtos.MemberAdminViewDto;
import com.multi.matchingbot.member.domain.entities.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-27T07:20:35+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberAdminViewDto toMemberAdminView(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberAdminViewDto memberAdminViewDto = new MemberAdminViewDto();

        memberAdminViewDto.setId( member.getId() );
        memberAdminViewDto.setName( member.getName() );
        memberAdminViewDto.setEmail( member.getEmail() );
        memberAdminViewDto.setGender( member.getGender() );
        memberAdminViewDto.setPhone( member.getPhone() );
        memberAdminViewDto.setStatus( member.getStatus() );
        memberAdminViewDto.setAgreePrivacy( member.getAgreePrivacy() );
        memberAdminViewDto.setAgreeService( member.getAgreeService() );
        memberAdminViewDto.setCreatedBy( member.getCreatedBy() );
        memberAdminViewDto.setCreatedAt( member.getCreatedAt() );
        memberAdminViewDto.setUpdatedBy( member.getUpdatedBy() );
        memberAdminViewDto.setUpdatedAt( member.getUpdatedAt() );

        memberAdminViewDto.setFormattedId( formatId(member.getId()) );

        return memberAdminViewDto;
    }
}
