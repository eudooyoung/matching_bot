package com.multi.matchingbot.admin.domain.enums;

public enum EndStatus {
    ALL,       // 전체
    OPEN,      // 진행 중 (endDate >= today)
    EXPIRED    // 마감됨 (endDate < today)
}