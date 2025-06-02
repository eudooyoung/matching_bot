package com.multi.matchingbot.attachedItem.service;

import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.attachedItem.util.ReportImageGenerator;
import com.multi.matchingbot.chatbot.ChatbotReportService;
import com.multi.matchingbot.chatbot.ReportDataBuilder;
import com.multi.matchingbot.common.domain.enums.ItemType;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachedItemService {

    private final ChatbotReportService chatbotReportService;
    private final ReportImageGenerator reportImageGenerator;
    private final FileService fileService;

    public AttachedItem saveReportImage(CompanyRegisterDto dto, Long companyId) {
        try {
            Map<String, Object> reportData = ReportDataBuilder.fromCompany(dto);

            Map<String, Object> parsed = chatbotReportService.generateReport(reportData);
            if (parsed == null || parsed.isEmpty()) {
                log.warn("⚠️ companyId={} - AI 응답 없음 또는 파싱 실패", companyId);
                return null;
            }
            reportData.putAll(parsed);

            BufferedImage image = reportImageGenerator.convertReportToImage(reportData);

            String extension = ".png";
            String baseName = "report-" + companyId;
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String systemName = baseName + "-" + uuid + extension;
            String originalName = baseName + extension;
            String path = "/upload/report/";

            FileMeta meta = FileMeta.builder()
                    .referenceId(companyId)
                    .itemType(ItemType.REPORT)
                    .originalName(originalName)
                    .systemName(systemName)
                    .path(path)
                    .build();

            return fileService.save(meta, image);

        } catch (Exception e) {
            log.error("!! 기업 ID={} 리포트 생성 실패: {}", companyId, e.getMessage(), e);
            return null;
        }
    }
}

