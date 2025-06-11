package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.BulkRequestDto;
import com.multi.matchingbot.admin.domain.BulkResponseDto;
import com.multi.matchingbot.admin.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
