package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.BulkRequestDto;
import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.service.*;
import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bulk")
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminBulkRestController {

    private final MemberAdminService memberAdminService;
    private final CompanyAdminService companyAdminService;
    private final ResumeAdminService resumeAdminService;
    private final JobAdminService jobAdminService;
    private final CommunityAdminService communityAdminService;
    private final AttachedItemService attachedItemService;

    @DeleteMapping("members")
    public ResponseEntity<BulkResponseDto> deactivateMembers(@RequestBody BulkRequestDto requestDto) {
        return ResponseEntity.ok(memberAdminService.deactivateBulk(requestDto.getIds()));
    }

    @PatchMapping("members")
    public ResponseEntity<BulkResponseDto> restoreMembers(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(memberAdminService.reactivateBulk(requestDto.getIds()));
    }

    @DeleteMapping("companies")
    public ResponseEntity<BulkResponseDto> deactivateCompanies(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(companyAdminService.deactivateBulk(requestDto.getIds()));
    }

    @PatchMapping("companies")
    public ResponseEntity<BulkResponseDto> restoreCompanies(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(companyAdminService.reactivateBulk(requestDto.getIds()));
    }

    @PostMapping("companies")
    public CompletableFuture<ResponseEntity<BulkResponseDto>> refreshReports(@RequestBody @Valid BulkRequestDto requestDto) {
        return attachedItemService.bulkSaveReportImageAsync(requestDto.getIds())
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("resumes")
    public ResponseEntity<BulkResponseDto> hardDeleteResumes(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(resumeAdminService.bulkHardDelete(requestDto.getIds()));
    }

    @DeleteMapping("jobs")
    public ResponseEntity<BulkResponseDto> hardDeleteJobs(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(jobAdminService.bulkHardDelete(requestDto.getIds()));
    }

    @DeleteMapping("communityPosts")
    public ResponseEntity<BulkResponseDto> hardDeleteCommunityPosts(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(communityAdminService.bulkHardDelete(requestDto.getIds()));
    }

}
