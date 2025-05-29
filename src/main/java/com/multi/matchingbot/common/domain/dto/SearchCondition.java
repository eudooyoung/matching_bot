package com.multi.matchingbot.common.domain.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class SearchCondition {
    private String keyword;     // 검색어
    private String status;      // (가입) 상태
    private String reportStatus;    // 보고서 추출 상태
    private String keywordsStatus;  // 키워드 추출 상태
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "DESC";

    public Pageable toPageable() {
        Sort.Direction dir = "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, sortBy));
    }
}
