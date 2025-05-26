package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("members/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable("id") Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("members/{id}/reactivate")
    public ResponseEntity<Void> reactiveMember(@PathVariable("id") Long id) {
        memberService.reactivate(id);
        return ResponseEntity.noContent().build();
    }
}
