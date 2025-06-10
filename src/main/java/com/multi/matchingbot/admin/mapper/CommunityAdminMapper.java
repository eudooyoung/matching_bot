package com.multi.matchingbot.admin.mapper;

import com.multi.matchingbot.admin.domain.view.CommunityAdminView;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.community.domain.CommunityPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommunityAdminMapper {
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "writerName", expression = "java(resolveWriterName(post))")
    @Mapping(target = "writerType", expression = "java(resolveWriterType(post))")
    @Mapping(target = "formattedId", expression = "java(CommunityAdminMapper.formattedId(post.getId()))")
    CommunityAdminView toDto(CommunityPost post);

    default String resolveWriterName(CommunityPost post) {
        if (post.getMember() != null && post.getMember().getNickname() != null) {
            return post.getMember().getNickname();
        } else if (post.getCompany() != null && post.getCompany().getName() != null) {
            return post.getCompany().getName();
        }
        return "(알 수 없음)";
    }

    default Role resolveWriterType(CommunityPost post) {
        if (post.getMember() != null) return Role.MEMBER;
        if (post.getCompany() != null) return Role.COMPANY;
        return Role.UNKNOWN;
    }

    static String formattedId(Long id) {
           return String.format("CP%05d", id);
       }
}
