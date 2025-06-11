package com.multi.matchingbot.community.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommunityCommentDto {
    private Long id;
    private String content;
    private String writerName;
    private String createdBy;
    private LocalDateTime createdAt;
    private Long memberId;
    private Long companyId;
    private String companyName;
    private String nickname;

public static CommunityCommentDto fromEntity(CommunityComment comment) {
    CommunityCommentDto dto = new CommunityCommentDto();
    dto.setId(comment.getId());
    dto.setContent(comment.getContent());
    dto.setCreatedBy(comment.getCreatedBy());
    dto.setCreatedAt(comment.getCreatedAt());

    if (comment.getMember() != null) {
        dto.setMemberId(comment.getMember().getId());

        dto.setNickname(comment.getMember().getNickname());

    } else if (comment.getCompany() != null) {
        dto.setCompanyId(comment.getCompany().getId());
        dto.setCompanyName(comment.getCompany().getName());
        dto.setWriterName(comment.getCompany().getName());
    } else {
        dto.setWriterName("알 수 없음");
    }

    return dto;
}

}
