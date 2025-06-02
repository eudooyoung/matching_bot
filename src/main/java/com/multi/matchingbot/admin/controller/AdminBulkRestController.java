package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.BulkRequestDto;
import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.service.CompanyAdminService;
import com.multi.matchingbot.admin.service.MemberAdminService;
import com.multi.matchingbot.admin.service.ResumeAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/bulk")
@RestController
public class AdminBulkRestController {

    private final MemberAdminService memberAdminService;
    private final CompanyAdminService companyAdminService;
    private final ResumeAdminService resumeAdminService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("members")
    public ResponseEntity<BulkResponseDto> deactivateMembers(@RequestBody BulkRequestDto requestDto) {
        return ResponseEntity.ok(memberAdminService.deactivateBulk(requestDto.getIds()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("members")
    public ResponseEntity<BulkResponseDto> restoreMembers(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(memberAdminService.reactivateBulk(requestDto.getIds()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("companies")
    public ResponseEntity<BulkResponseDto> deactivateCompanies(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(companyAdminService.deactivateBulk(requestDto.getIds()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("companies")
    public ResponseEntity<BulkResponseDto> restoreCompanies(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(companyAdminService.reactivateBulk(requestDto.getIds()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("resumes")
    public ResponseEntity<BulkResponseDto> hardDeleteResumes(@RequestBody @Valid BulkRequestDto requestDto) {
        return ResponseEntity.ok(resumeAdminService.bulkHardDelete(requestDto.getIds()));
    }

}
