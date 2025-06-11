package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.service.*;
import com.multi.matchingbot.attachedItem.service.AttachedItemService;
import com.multi.matchingbot.company.domain.CompanyUpdateReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

    private final MemberAdminService memberAdminService;
    private final CompanyAdminService companyAdminService;
    private final ResumeAdminService resumeAdminService;
    private final JobAdminService jobAdminService;
    private final CommunityAdminService communityAdminService;
    private final AttachedItemService attachedItemService;

    /**
     * 개인 회원 비활성화
     *
     * @param id
     * @return
     */
    @DeleteMapping("members/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable("id") Long id) {
        memberAdminService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 개인 회원 활성화
     *
     * @param id
     * @return
     */
    @PatchMapping("members/{id}/reactivate")
    public ResponseEntity<Void> reactivateMember(@PathVariable("id") Long id) {
        memberAdminService.reactivate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 기업 회원 비활성화
     *
     * @param id
     * @return
     */
    @DeleteMapping("companies/{id}")
    public ResponseEntity<Void> deactivateCompany(@PathVariable("id") Long id) {
        companyAdminService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 기업 회원 활성화
     *
     * @param id
     * @return
     */
    @PatchMapping("companies/{id}/reactivate")
    public ResponseEntity<Void> reactivateCompany(@PathVariable("id") Long id) {
        companyAdminService.reactivate(id);
        return ResponseEntity.noContent().build();

    }

    /**기업 회원 보고서 추출 관리자용
     *
     * @param companyId
     * @return
     */
    @PostMapping("/company/{companyId}/refresh-report")
    public ResponseEntity<Void> refreshCompanyReport(@PathVariable Long companyId) {
        CompanyUpdateReportDto dto = companyAdminService.getReportSource(companyId);
        attachedItemService.saveReportImage(dto, companyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("resumes/{id}")
    public ResponseEntity<Void> deleteResumeHard(@PathVariable("id") Long id) {
        resumeAdminService.deleteHard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("jobs/{id}")
    public ResponseEntity<Void> deleteJobHard(@PathVariable("id") Long id) {
        jobAdminService.deleteHard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("community/{id}")
    public ResponseEntity<Void> deleteCommunityPostHard(@PathVariable("id") Long id) {
        log.info("게시글 삭제 요청 수신");
        communityAdminService.deleteHard(id);
        return ResponseEntity.noContent().build();
    }


}
