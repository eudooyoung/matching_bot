package com.multi.matchingbot.attachedItem.service;

import com.multi.matchingbot.attachedItem.AttachedItemRepository;
import com.multi.matchingbot.attachedItem.ReportImageGenerator;
import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.attachedItem.domain.ReportType;
import com.multi.matchingbot.chatbot.ChatbotReportService;
import com.multi.matchingbot.chatbot.ReportDataBuilder;
import com.multi.matchingbot.common.domain.enums.ItemType;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachedItemService {

    private final ChatbotReportService chatbotReportService;
    private final ReportImageGenerator reportImageGenerator;
    private final FileService fileService;
    private final AttachedItemRepository attachedItemRepository;

    /**
     * @param dto       기업 회원 가입 정보 dto
     * @param companyId 기업 회원 id
     * @return 저장된 AttachedItem 엔티티를 리턴함
     */
    public List<AttachedItem> saveReportImage(CompanyRegisterDto dto, Long companyId) {
        try {
            Map<String, Object> reportData = ReportDataBuilder.fromCompany(dto);

            Map<String, Object> parsed = chatbotReportService.generateReport(reportData);
            if (parsed == null || parsed.isEmpty()) {
                log.warn("⚠️ companyId={} - AI 응답 없음 또는 파싱 실패", companyId);
                return null;
            }
            reportData.putAll(parsed);

           /* BufferedImage image = reportImageGenerator.convertReportToImage(reportData, reportType);

            String extension = ".png";
            String baseName = reportType.getFilePrefix();
            String uuid = UUID.randomUUID().toString().substring(0, 16);
            String systemName = baseName + "-" + uuid + extension;
            String originalName = baseName + extension;
            String path = "upload/company/" + companyId + "/" + systemName;

            FileMeta meta = FileMeta.builder()
                    .referenceId(companyId)
                    .itemType(reportType.getItemType())
                    .originalName(originalName)
                    .systemName(systemName)
                    .path(path)
                    .build();

            return fileService.save(meta, image);*/

            List<AttachedItem> savedItems = new ArrayList<>();

            for (ReportType reportType : List.of(ReportType.FULL, ReportType.SUMMARY)) {
                BufferedImage image = reportImageGenerator.convertReportToImage(reportData, reportType);

                String extension = ".png";
                String baseName = reportType.getFilePrefix();
                String uuid = UUID.randomUUID().toString().substring(0, 16);
                String systemName = baseName + "-" + uuid + extension;
                String originalName = baseName + extension;
                String path = "upload/company/" + companyId + "/" + systemName;

                FileMeta meta = FileMeta.builder()
                        .referenceId(companyId)
                        .itemType(reportType.getItemType())
                        .originalName(originalName)
                        .systemName(systemName)
                        .path(path)
                        .build();

                AttachedItem saved = fileService.save(meta, image);
                savedItems.add(saved);
            }

            return savedItems;

        } catch (Exception e) {
            log.error("!! 기업 ID={} 리포트 생성 실패: {}", companyId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param companyId 기업회원id: attachedItem 테이블에 저장된 referenceId
     * @return AttachedItem 엔티티를 Optional객체에 담아서 리턴
     */
    public Optional<AttachedItem> findReportForCompany(Long companyId) {
        return attachedItemRepository.findByReferenceIdAndItemTypeAndStatus(companyId, ItemType.REPORT, Yn.Y);
    }

    public Optional<AttachedItem> findSummaryForCompany(Long companyId) {
        return attachedItemRepository.findByReferenceIdAndItemTypeAndStatus(companyId, ItemType.SUMMARY, Yn.Y);
    }
}
