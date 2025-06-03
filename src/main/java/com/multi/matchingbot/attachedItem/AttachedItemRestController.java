package com.multi.matchingbot.attachedItem;

import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attached")
@RequiredArgsConstructor
public class AttachedItemRestController {

    private final AttachedItemService attachedItemService;

    @GetMapping("/report-image-path/{companyId}")
    public ResponseEntity<String> getReportImagePath(@PathVariable("companyId") Long companyId) {
        return attachedItemService.findReportForCompany(companyId)
                .map(report -> ResponseEntity.ok("/" + report.getPath()))
                .orElse(ResponseEntity.ok("/img/fallback.jpeg")); // 기본 대체 이미지
    }

    @GetMapping("/summary-image-path/{companyId}")
    public ResponseEntity<String> getSummaryImagePath(@PathVariable("companyId") Long companyId) {
        return attachedItemService.findSummaryForCompany(companyId)
                .map(summary -> ResponseEntity.ok("/" + summary.getPath()))
                .orElse(ResponseEntity.ok("/img/fallback.jpg")); // 없을 경우 기본 이미지
    }
}
