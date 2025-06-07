package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.service.ResumeBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job/resume-bookmark")
public class ResumeBookmarkController {

    private final ResumeBookmarkService resumeBookmarkService;

    @PostMapping("/{resumeId}/company/{companyId}")
    public ResponseEntity<Void> addBookmark(@PathVariable("resumeId") Long resumeId,
                                            @PathVariable("companyId") Long companyId) {
        resumeBookmarkService.addBookmark(resumeId, companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/toggle")
    @ResponseBody
    public Map<String, Boolean> toggleBookmark(@RequestParam(name = "resumeId") Long resumeId,
                                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long companyId = userDetails.getCompanyId(); // 회사 로그인 사용자
        boolean bookmarked = resumeBookmarkService.toggleBookmark(companyId, resumeId);
        return Map.of("bookmarked", bookmarked);
    }

    @DeleteMapping("/{resumeId}/company/{companyId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("resumeId") Long resumeId, @PathVariable("companyId") Long companyId) {
        resumeBookmarkService.deleteByResumeIdAndCompanyId(resumeId, companyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk/company/{companyId}")
    public ResponseEntity<Void> deleteSelectedBookmarks(@RequestBody List<Long> resumeIds,
                                                        @PathVariable("companyId") Long companyId) {
        resumeBookmarkService.deleteAllByResumeIdsAndCompanyId(resumeIds, companyId);
        return ResponseEntity.ok().build();
    }
}