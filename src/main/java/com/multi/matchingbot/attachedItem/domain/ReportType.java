package com.multi.matchingbot.attachedItem.domain;

import com.multi.matchingbot.common.domain.enums.ItemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    FULL(
            "chatbot/evaluation-report",
            ItemType.REPORT,
            "report-full",
            100),

    SUMMARY("chatbot/evaluation-summary",
            ItemType.SUMMARY,
            "report-summary",
            800);

    /**
     * 타입 분기 변수
     * templatePath: html 템플릿 경로
     * ItemType: AttachedItem 테이블에 저장될 타입명(이넘)
     * filePrefix: 파일명 접두어
     * cropBottom: 하단 영역 삭제 길이
     */
    private final String templatePath;
    private final ItemType itemType;
    private final String filePrefix;
    private final int cropBottom;

    public static final int DPI = 200;      // 화질
    public static final int CROP_TOP = 60;      // 상단 삭제
}
