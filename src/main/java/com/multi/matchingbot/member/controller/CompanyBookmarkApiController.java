package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member/company-bookmark")
public class CompanyBookmarkApiController {

    @Autowired
    private CompanyBookmarkService companyBookmarkService;

    // 북마크 추가
    @PostMapping("/{companyId}")
    public ResponseEntity<String> addBookmark(@PathVariable Long companyId,
                                              @AuthenticationPrincipal MBotUserDetails userDetails) {
        try {
            companyBookmarkService.addBookmark(userDetails.getMemberId(), companyId);
            return ResponseEntity.ok("북마크가 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("북마크 추가에 실패했습니다.");
        }
    }

    // 북마크 삭제 (단일)
    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> removeBookmark(@PathVariable Long companyId,
                                                 @AuthenticationPrincipal MBotUserDetails userDetails) {
        try {
            companyBookmarkService.removeBookmark(userDetails.getMemberId(), companyId);
            return ResponseEntity.ok("북마크가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("북마크 삭제에 실패했습니다.");
        }
    }

    // 북마크 다중 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> removeBookmarks(@RequestBody List<Long> companyIds,
                                                  @AuthenticationPrincipal MBotUserDetails userDetails) {
        try {
            companyBookmarkService.removeBookmarks(userDetails.getMemberId(), companyIds);
            return ResponseEntity.ok("선택한 북마크가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("북마크 삭제에 실패했습니다.");
        }
    }

    // 북마크 상태 토글
    @PostMapping("/toggle/{companyId}")
    public ResponseEntity<String> toggleBookmark(@PathVariable Long companyId,
                                                 @AuthenticationPrincipal MBotUserDetails userDetails) {
        try {
            boolean isBookmarked = companyBookmarkService.toggleBookmark(userDetails.getMemberId(), companyId);
            String message = isBookmarked ? "북마크가 추가되었습니다." : "북마크가 삭제되었습니다.";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("북마크 처리에 실패했습니다.");
        }
    }
} 