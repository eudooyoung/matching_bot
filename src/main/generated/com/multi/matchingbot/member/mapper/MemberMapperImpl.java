package com.multi.matchingbot.member.mapper;

import com.multi.matchingbot.member.domain.dtos.MemberAdminView;
import com.multi.matchingbot.member.domain.entities.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T09:50:36+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberAdminView toMemberAdminView(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberAdminView memberAdminView = new MemberAdminView();

        memberAdminView.setId( member.getId() );
        memberAdminView.setName( member.getName() );
        memberAdminView.setEmail( member.getEmail() );
        memberAdminView.setGender( member.getGender() );
        memberAdminView.setPhone( member.getPhone() );
        memberAdminView.setStatus( member.getStatus() );
        memberAdminView.setAgreePrivacy( member.getAgreePrivacy() );
        memberAdminView.setAgreeService( member.getAgreeService() );
        memberAdminView.setCreatedBy( member.getCreatedBy() );
        memberAdminView.setCreatedAt( member.getCreatedAt() );
        memberAdminView.setUpdatedBy( member.getUpdatedBy() );
        memberAdminView.setUpdatedAt( member.getUpdatedAt() );

        memberAdminView.setFormattedId( MemberMapper.formatId(member.getId()) );

        return memberAdminView;
    }
}
