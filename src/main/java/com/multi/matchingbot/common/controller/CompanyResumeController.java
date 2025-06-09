package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.member.domain.dto.ResumeDto;
import com.multi.matchingbot.member.domain.entity.Resume;
import com.multi.matchingbot.member.service.ResumeService;
import com.multi.matchingbot.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMPANY')")
public class CompanyResumeController {

    private final ResumeService resumeService;
    private final NotificationService notificationService;

    @GetMapping
    public String resumeList(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "6") int size,
                             @AuthenticationPrincipal MBotUserDetails userDetails,
                             Model model) {

        log.info("📄 resumeList() 컨트롤러 도달!");

        if (userDetails != null) {
            log.info("현재 사용자 ROLE: {}", userDetails.getRole());
            model.addAttribute("role", userDetails.getRole().name());  // ✅ role 전달
        } else {
            model.addAttribute("role", null);  // 예외적으로 null 처리
        }

        int pageIndex = Math.max(0, page - 1);
        Page<ResumeDto> resumePage = resumeService.getPageResumes(PageRequest.of(pageIndex, size));
        List<Long> bookmarkedIds = resumeService.findBookmarkedResumeIdsByCompanyId(userDetails.getCompanyId());

        resumePage.forEach(dto -> dto.setBookmarked(bookmarkedIds.contains(dto.getId())));

        model.addAttribute("resumeList", resumePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resumePage.getTotalPages());

        return "resume/list";
    }


    @GetMapping("/{id}")
    public String resumeDetail(@PathVariable("id") Long id,
                               Model model,
                               @AuthenticationPrincipal Object user,
                               @AuthenticationPrincipal MBotUserDetails userDetails) {
        log.info("📄 resumeDetail() 호출됨 - 이력서 ID: {}", id);

        try {
            Resume resume = resumeService.findById(id);
            ResumeDto resumeDto = ResumeDto.fromEntity(resume); // 또는 직접 toDto 작성
            model.addAttribute("resume", resumeDto);

            // 이력서 열람 알림 생성
            Long resumeOwnerId = resume.getMember().getId(); // 이력서 주인
            String companyName = userDetails.getCompanyName(); // 로그인한 기업 이름
            notificationService.sendResumeViewedNotification(resumeOwnerId, companyName, resume.getTitle());
            // ✅ templates/resume/detail.html 존재해야 함
            // ✅ role 전달
            if (user instanceof MBotUserDetails details) {
                log.info("현재 사용자 ROLE: {}", details.getRole());
                model.addAttribute("role", details.getRole().name());
            } else {
                model.addAttribute("role", null);
            }

            return "member/resume-view";

        } catch (EntityNotFoundException e) {
            log.warn("❌ 해당 이력서를 찾을 수 없음 - ID: {}", id);
            return "error/404"; // 또는 사용자 정의 에러 페이지
        }
    }
}
