package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.dto.ResumeBookmarkDto;
import com.multi.matchingbot.job.service.ResumeBookmarkService;
import com.multi.matchingbot.member.domain.dtos.ResumeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume-bookmarks")
@RequiredArgsConstructor
public class ResumeBookmarkController {

    private final ResumeBookmarkService resumeBookmarkService;

    // 즐겨찾기 토글
    @PostMapping
    public void toggleBookmark(@RequestBody ResumeBookmarkDto dto) {
        resumeBookmarkService.toggleBookmark(dto.getCompanyId(), dto.getResumeId());
    }

    // 기업이 즐겨찾기한 이력서 목록 (숫자만 허용)
    @GetMapping("/{companyId:[0-9]+}")
    public List<ResumeDto> getBookmarks(@PathVariable Long companyId) {
        return resumeBookmarkService.getBookmarks(companyId);
    }
}
