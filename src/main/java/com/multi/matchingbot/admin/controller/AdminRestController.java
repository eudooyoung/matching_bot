package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.CompanyAdminService;
import com.multi.matchingbot.admin.service.MemberAdminService;
import com.multi.matchingbot.admin.service.ResumeAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final MemberAdminService memberAdminService;
    private final CompanyAdminService companyAdminService;
    private final ResumeAdminService resumeAdminService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("members/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable("id") Long id) {
        memberAdminService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("members/{id}/reactivate")
    public ResponseEntity<Void> reactivateMember(@PathVariable("id") Long id) {
        memberAdminService.reactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("companies/{id}")
    public ResponseEntity<Void> deactivateCompany(@PathVariable("id") Long id) {
        companyAdminService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("companies/{id}/reactivate")
    public ResponseEntity<Void> reactivateCompany(@PathVariable("id") Long id) {
        companyAdminService.reactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("resumes/{id}")
    public ResponseEntity<Void> deleteResumeHard(@PathVariable("id") Long id) {
        resumeAdminService.deleteHard(id);
        return ResponseEntity.noContent().build();
    }
}
