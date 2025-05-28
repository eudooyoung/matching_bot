package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.member.service.ResumeService;
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

    private final MemberService memberService;
    private final CompanyService companyService;
    private final ResumeService resumeService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("members/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable("id") Long id) {
        memberService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("members/{id}/reactivate")
    public ResponseEntity<Void> reactivateMember(@PathVariable("id") Long id) {
        memberService.reactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("resumes/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
