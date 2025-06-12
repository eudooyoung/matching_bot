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
    private Long companyId;
    private String companyName;
    private String nickname;
    private Long writerId;  // 개인/기업 통합된 작성자 ID




    private List<CommunityCommentDto> comments;

    public static CommunityPostDto fromEntity(CommunityPost post) {
        CommunityPostDto dto = new CommunityPostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setViews(post.getViews());

        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }

        if (post.getMember() != null) {
            dto.setMemberId(post.getMember().getId());
            dto.setNickname(post.getMember().getNickname());
            dto.setWriterName(post.getMember().getNickname());
            dto.setWriterId(post.getMember().getId());
        }

        if (post.getCompany() != null) {
            dto.setCompanyId(post.getCompany().getId());
            dto.setCompanyName(post.getCompany().getName());
            dto.setWriterName(post.getCompany().getName());
            dto.setWriterId(post.getCompany().getId());
        }

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