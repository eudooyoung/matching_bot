package com.multi.matchingbot.admin.domain;

import com.multi.matchingbot.admin.domain.enums.EndStatus;
import com.multi.matchingbot.career.domain.CareerType;
import com.multi.matchingbot.common.domain.enums.Role;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class AdminSearchCondition {
    @Size(max = 50)
    private String keyword;     // 검색어

    private String status;      // (가입) 상태
    private String reportStatus;    // 보고서 추출 상태
    private String keywordsStatus;  // 키워드 추출 상태
    private CareerType careerType;  // 이력서 타입: 신입/경력
    private EndStatus endStatus = EndStatus.ALL;  // 채용 공고 진행 상태
    private Long categoryId; // 커뮤니티 카테고리 아이디
    private Role writerType;


    private int page = 0;
    private int size = 15;
    private String sortBy = "id";
    private String direction = "DESC";

    public Pageable toPageable() {
        Sort.Direction dir = "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, sortBy));
    }
}
