package com.multi.matchingbot.attachedItem.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import com.multi.matchingbot.member.service.JobBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/attached")
@RequiredArgsConstructor
public class AttachedItemController {

    private final JobService jobService;
    private final CompanyService companyService;
    private final CompanyBookmarkService companyBookmarkService;
    private final JobBookmarkService jobBookmarkService;

    @GetMapping("/company/{postingCompanyId}")
    public String goPostingCompanyHome(@PathVariable("postingCompanyId") Long postingCompanyId,
                                       @AuthenticationPrincipal MBotUserDetails userDetails,
                                       Model model) {
        model.addAttribute("companyId", postingCompanyId);

        Company company = companyService.findById(postingCompanyId);
        model.addAttribute("company", company);

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(postingCompanyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);

        // 구직자인 경우 북마크 상태 전달
        if (userDetails != null && "MEMBER".equals(userDetails.getRole())) {
            Long memberId = userDetails.getMemberId();

            // 기업 북마크 상태
            boolean isCompanyBookmarked = companyBookmarkService.isBookmarked(memberId, postingCompanyId);
            model.addAttribute("isCompanyBookmarked", isCompanyBookmarked);

            // 채용공고 북마크 상태 리스트
            List<Long> bookmarkedJobIds = jobBookmarkService.getBookmarkedJobIds(memberId);
            model.addAttribute("bookmarkedJobIds", bookmarkedJobIds);
        } else {
            // 구직자가 아닌 경우 기본값 설정 (안전장치)
            model.addAttribute("isCompanyBookmarked", false);
            model.addAttribute("bookmarkedJobIds", java.util.Collections.emptyList());
        }

        return "company/index";
    }

    // 기업 북마크 추가/제거 토글 API
    @PostMapping("/api/company-bookmark/toggle")
    @ResponseBody
    public ResponseEntity<String> toggleCompanyBookmark(@RequestParam Long companyId,
                                                        @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();

        if (companyBookmarkService.isBookmarked(memberId, companyId)) {
            companyBookmarkService.deleteCompanyBookmark(memberId, companyId);
            return ResponseEntity.ok("removed");
        } else {
            companyBookmarkService.addCompanyBookmark(memberId, companyId);
            return ResponseEntity.ok("added");
        }
    }

    // 채용공고 북마크 추가/제거 토글 API
    @PostMapping("/api/job-bookmark/toggle")
    @ResponseBody
    public ResponseEntity<String> toggleJobBookmark(@RequestParam Long jobId,
                                                    @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();

        if (jobBookmarkService.isBookmarked(memberId, jobId)) {
            jobBookmarkService.removeJobBookmark(memberId, jobId);
            return ResponseEntity.ok("removed");
        } else {
            jobBookmarkService.addJobBookmark(memberId, jobId);
            return ResponseEntity.ok("added");
        }
    }
}
