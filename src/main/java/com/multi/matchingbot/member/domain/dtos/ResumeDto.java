package com.multi.matchingbot.member.domain.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResumeDto {

    private Long id;                    // 이력서 ID
    private String title;              // 이력서 제목
    private String skillAnswer;        // 보유 기술 설명
    private String traitAnswer;        // 성격 및 장점 설명
    private String skillKeywords;      // 기술 키워드
    private String talentKeywords;     // 인재상 키워드
    private String keywordsStatus;     // 키워드 추출 상태 (Y/N)
    private LocalDateTime createdAt;   // 작성일
    private String memberName;         // 작성자 이름 (member 테이블 join 필요)
    private boolean bookmarked;        // 관심 이력서 여부 (join or 별도 체크)

    public ResumeDto(Long id, String title, LocalDateTime createdAt, String memberName) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.memberName = memberName;
    }
}
