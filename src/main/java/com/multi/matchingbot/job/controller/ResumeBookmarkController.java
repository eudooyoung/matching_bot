package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.service.ResumeBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job/resume-bookmark")
public class ResumeBookmarkController {

    private final ResumeBookmarkService resumeBookmarkService;

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