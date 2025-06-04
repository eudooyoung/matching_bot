package com.multi.matchingbot.attachedItem;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.company.domain.CompanyUpdateReportDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attached")
@RequiredArgsConstructor
public class AttachedItemRestController {

    private final AttachedItemService attachedItemService;

    @GetMapping("/report-image-path/{companyId}")
    public ResponseEntity<String> getReportImagePath(@PathVariable("companyId") Long companyId) {
        return attachedItemService.findReportForCompany(companyId)
                .map(report -> ResponseEntity.ok("/" + report.getPath()))
                .orElse(ResponseEntity.ok("/img/fallback.jpg")); // 기본 대체 이미지
    }

    @GetMapping("/summary-image-path/{companyId}")
    public ResponseEntity<String> getSummaryImagePath(@PathVariable("companyId") Long companyId) {
        return attachedItemService.findSummaryForCompany(companyId)
                .map(summary -> ResponseEntity.ok("/" + summary.getPath()))
                .orElse(ResponseEntity.ok("/img/fallback.jpg")); // 없을 경우 기본 이미지
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/{companyId}/refresh-report")
    public ResponseEntity<Void> refreshReport(@PathVariable("companyId") Long companyId,
                                              @Valid @RequestBody CompanyUpdateReportDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
        }

        // AI 평가 리포트 생성 및 저장
        attachedItemService.saveReportImage(dto, companyId);

        return ResponseEntity.ok().build();
    }
}
