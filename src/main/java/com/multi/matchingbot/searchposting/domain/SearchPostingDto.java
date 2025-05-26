package com.multi.matchingbot.searchposting.domain;

import com.multi.matchingbot.mapposting.domain.MapPosting;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPostingDto {

    private Long id;
    private String title;
    private String address;
    private String requiredSkills;
    private String companyName;
    private LocalDate endDate;

    //fromEntity Entity를 Dto로 변환해주는 패턴
    public static SearchPostingDto fromEntity(MapPosting posting) {
        return SearchPostingDto.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .address(posting.getAddress())
                .requiredSkills(posting.getRequiredSkills())
                .companyName(posting.getCompany().getName())
                .build();
    }
}
