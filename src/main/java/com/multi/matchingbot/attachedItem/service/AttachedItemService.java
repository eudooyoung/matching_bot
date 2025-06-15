package com.multi.matchingbot.attachedItem.service;

import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.service.CompanyAdminService;
import com.multi.matchingbot.attachedItem.AttachedItemRepository;
import com.multi.matchingbot.attachedItem.ReportImageGenerator;
import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.attachedItem.domain.ReportType;
import com.multi.matchingbot.chatbot.service.ChatbotReportService;
import com.multi.matchingbot.chatbot.util.ReportDataBuilder;
import com.multi.matchingbot.common.domain.enums.ItemType;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.CompanyRegisterDto;
import com.multi.matchingbot.company.domain.CompanyUpdateReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachedItemService {

    private final CompanyAdminService companyAdminService;
    private final ChatbotReportService chatbotReportService;
    private final FileService fileService;
    private final S3FileService s3FileService;
    private final ReportImageGenerator reportImageGenerator;
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

            List<AttachedItem> savedItems = new ArrayList<>();

            for (ReportType reportType : List.of(ReportType.FULL, ReportType.SUMMARY)) {
                BufferedImage image = reportImageGenerator.convertReportToImage(reportData, reportType);

                String extension = ".jpg";
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

                AttachedItem saved = s3FileService.save(meta, image);
                savedItems.add(saved);
            }

            return savedItems;

        } catch (Exception e) {
            log.error("!! 기업 ID={} 리포트 생성 실패: {}", companyId, e.getMessage(), e);
            return null;
        }
    }

    public List<AttachedItem> saveReportImage(CompanyUpdateReportDto dto, Long companyId) {
        try {
            Map<String, Object> reportData = ReportDataBuilder.fromCompany(dto);

            Map<String, Object> parsed = chatbotReportService.generateReport(reportData);
            if (parsed == null || parsed.isEmpty()) {
                log.warn("⚠️ companyId={} - AI 응답 없음 또는 파싱 실패", companyId);
                return null;
            }
            reportData.putAll(parsed);

            List<AttachedItem> savedItems = new ArrayList<>();

            for (ReportType reportType : List.of(ReportType.FULL, ReportType.SUMMARY)) {
                BufferedImage image = reportImageGenerator.convertReportToImage(reportData, reportType);

                String extension = ".jpg";
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

                AttachedItem saved = s3FileService.save(meta, image);
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

    @Async
        public void saveReportImageAsync(CompanyRegisterDto dto, Long companyId) {
            saveReportImage(dto, companyId);
        }

    @Async
    public void saveReportImageAsync(CompanyUpdateReportDto dto, Long companyId) {
        saveReportImage(dto, companyId);
    }


    public BulkResponseDto bulkSaveReportImage(List<Long> companyIds) {
        List<Long> failed = new ArrayList<>();
        for (Long id : companyIds) {
            try {
                CompanyUpdateReportDto dto = companyAdminService.getReportSource(id);
                saveReportImage(dto, id);
            } catch (Exception e) {
                failed.add(id);
                log.warn("ID {} 보고서 생성 실패: {}", id, e.getMessage());
            }
        }
        boolean isSuccess = failed.isEmpty();
        return new BulkResponseDto(isSuccess, failed);
    }
}
