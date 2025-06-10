package com.multi.matchingbot.attachedItem.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import com.multi.matchingbot.member.service.JobBookmarkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/attached")
@RequiredArgsConstructor
@Slf4j
public class AttachedItemController {

    private final JobService jobService;
    private final CompanyService companyService;
    private final CompanyBookmarkService companyBookmarkService;
    private final JobBookmarkService jobBookmarkService;

    @GetMapping("/company/{postingCompanyId}")
    public String goPostingCompanyHome(@PathVariable("postingCompanyId") Long postingCompanyId,
                                       @AuthenticationPrincipal MBotUserDetails userDetails,
                                       Model model) {
        log.info("Company page access - companyId: {}", postingCompanyId);
        log.info("User details: {}", userDetails != null ? userDetails.getUsername() + " role: " + userDetails.getRole() : "null");

        // 역할 디버깅 정보 추가
        if (userDetails != null) {
            log.info("Role type: {}", userDetails.getRole().getClass().getName());
            log.info("Role toString: '{}'", userDetails.getRole().toString());
            log.info("Role equals MEMBER: {}", "MEMBER".equals(userDetails.getRole().toString()));
            log.info("Role enum comparison: {}", Role.MEMBER.equals(userDetails.getRole()));
        }

        model.addAttribute("companyId", postingCompanyId);

        Company company = companyService.findById(postingCompanyId);
        model.addAttribute("company", company);

        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(postingCompanyId, PageRequest.of(0, 20));
        model.addAttribute("jobPage", jobPage);

        // 구직자인 경우 북마크 상태 전달
        if (userDetails != null && "MEMBER".equals(userDetails.getRole().toString())) {
            Long memberId = userDetails.getMemberId();
            log.info("Member access - memberId: {}", memberId);

            // 기업 북마크 상태
            boolean isCompanyBookmarked = companyBookmarkService.isBookmarked(memberId, postingCompanyId);
            log.info("Company bookmark status: {}", isCompanyBookmarked);
            model.addAttribute("isCompanyBookmarked", isCompanyBookmarked);

            // 채용공고 북마크 상태 리스트
            List<Long> bookmarkedJobIds = jobBookmarkService.getBookmarkedJobIds(memberId);
            log.info("Bookmarked job IDs: {}", bookmarkedJobIds);
            model.addAttribute("bookmarkedJobIds", bookmarkedJobIds);
        } else {
            log.info("Non-member access or not authenticated - setting default values");
            // 구직자가 아닌 경우 기본값 설정 (안전장치)
            model.addAttribute("isCompanyBookmarked", false);
            model.addAttribute("bookmarkedJobIds", java.util.Collections.emptyList());
        }

        return "company/index";
    }

    // 기업 북마크 추가/제거 토글 API
    @PostMapping("/api/company-bookmark/toggle")
    @ResponseBody
    public ResponseEntity<String> toggleCompanyBookmark(@RequestParam("companyId") Long companyId,
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
    public ResponseEntity<String> toggleJobBookmark(@RequestParam("jobId") Long jobId,
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
