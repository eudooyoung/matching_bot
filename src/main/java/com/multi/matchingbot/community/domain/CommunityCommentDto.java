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

    public static CommunityCommentDto fromEntity(CommunityComment comment) {
        CommunityCommentDto dto = new CommunityCommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setWriterName(comment.getMember().getName()); // 또는 getUsername()
        dto.setCreatedBy(comment.getCreatedBy());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setMemberId(comment.getMember().getId());
        return dto;
    }
}
