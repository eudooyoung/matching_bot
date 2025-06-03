// ✅ CommunityPostDto.java
package com.multi.matchingbot.community.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostDto {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long memberId;
    private String title;
    private String content;
    private int views;
    private String createdBy;
    private LocalDateTime createdAt;
    private String writerName; // ✅ 추가

    private List<CommunityCommentDto> comments;

    public static CommunityPostDto fromEntity(CommunityPost post) {
        CommunityPostDto dto = new CommunityPostDto();
        dto.setId(post.getId());

        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }

        if (post.getMember() != null) {
            dto.setMemberId(post.getMember().getId());
            dto.setWriterName(post.getMember().getName());
        }

        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setViews(post.getViews());
        dto.setCreatedBy(post.getCreatedBy());
        dto.setCreatedAt(post.getCreatedAt());

        // ✅ 댓글 리스트 변환 추가
        if (post.getComments() != null) {
            dto.setComments(
                    post.getComments().stream()
                            .map(CommunityCommentDto::fromEntity)
                            .toList()
            );
        }

        return dto;
    }

}