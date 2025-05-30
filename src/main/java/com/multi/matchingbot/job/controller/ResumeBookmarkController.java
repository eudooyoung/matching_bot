package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.service.ResumeBookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/bookmarks")
public class ResumeBookmarkController {

    private final ResumeBookmarkService resumeBookmarkService;

    public ResumeBookmarkController(ResumeBookmarkService resumeBookmarkService) {
        this.resumeBookmarkService = resumeBookmarkService;
    }

    @DeleteMapping("/job/resume-bookmark/delete")
    public ResponseEntity<?> deleteBookmark(@RequestParam Long companyId,
                                            @RequestParam Long resumeId) {
        resumeBookmarkService.deleteBookmark(companyId, resumeId);
        return ResponseEntity.ok().build();
    }


    private Long getCompanyIdFromPrincipal(Principal principal) {
        // principal.getName() → username or companyId로 가정
        // 실제 구현에 맞게 커스터마이징 필요
        return Long.parseLong(principal.getName());
    }
}

