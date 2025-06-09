package com.multi.matchingbot.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multi.matchingbot.admin.mapper.MemberAdminMapper;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.member.domain.dto.MemberProfileUpdateDto;
import com.multi.matchingbot.member.domain.dto.MemberUpdateDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import com.multi.matchingbot.member.service.JobBookmarkService;
import com.multi.matchingbot.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private final MemberService memberService;
    private final MemberAdminMapper memberAdminMapper;
    private final CompanyBookmarkService companyBookmarkService;
    private final JobBookmarkService jobBookmarkService;

    public MemberController(MemberService memberService, MemberAdminMapper memberAdminMapper, CompanyBookmarkService companyBookmarkService, JobBookmarkService jobBookmarkService) {
        this.memberService = memberService;
        this.memberAdminMapper = memberAdminMapper;
        this.companyBookmarkService = companyBookmarkService;
        this.jobBookmarkService = jobBookmarkService;
    }

    // 구직자 마이페이지 진입
    @GetMapping("/mypage")
    public String showMypage(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getId();
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "member/member-mypage";
    }

    // 개인정보 수정 페이지
    @GetMapping("/edit-profile")
    public String editProfileForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getId();
        model.addAttribute("member", memberService.findById(memberId));
        return "member/edit-profile";
    }

    // 프로필 편집 페이지 (새 경로)
    @GetMapping("/profile_edit")
    public String profileEditForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "member/profile-edit";
    }

    // 개인정보 수정 처리
    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("phone1") String phone1,
                                @RequestParam("phone2") String phone2,
                                @RequestParam("phone3") String phone3,
                                @ModelAttribute("member") MemberUpdateDto memberDto,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal MBotUserDetails userDetails) {

        memberDto.setPhone(phone1 + phone2 + phone3);  // 조합하여 저장

        if (bindingResult.hasErrors()) {
            return "member/edit-profile";
        }

        memberService.update(memberDto, userDetails.getMemberId());
        return "redirect:/member/mypage";
    }

    // 프로필 편집 처리 (새 경로)
    @PostMapping("/profile_edit")
    public String updateMemberProfile(@ModelAttribute MemberProfileUpdateDto updateDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal MBotUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "member/profile-edit";
        }

        updateDto.setId(userDetails.getMemberId());
        memberService.updateProfile(updateDto, userDetails.getMemberId());
        return "redirect:/member/mypage";
    }

    // 관심 기업 관리
    @GetMapping("/company-bookmark")
    public String showCompanyBookmarkPage(@AuthenticationPrincipal MBotUserDetails userDetails, Model model,
                                          @PageableDefault(size = 10) Pageable pageable){
        Long memberId = userDetails.getMemberId();
        model.addAttribute("memberId", memberId);

        Page<CompanyUpdateDto> companyPage = companyBookmarkService.getBookmarkedCompanys(memberId, pageable);
        model.addAttribute("companyPage", companyPage);

        return "member/company-bookmark";
    }

    // 관심 채용공고 관리
    @GetMapping("/job-bookmark")
    public String showJobBookmarkPage(@AuthenticationPrincipal MBotUserDetails userDetails, Model model,
                                      @PageableDefault(size = 10) Pageable pageable){
        Long memberId = userDetails.getMemberId();
        model.addAttribute("memberId", memberId);

        Page<JobDto> jobPage = jobBookmarkService.getBookmarkedJobs(memberId, pageable);
        model.addAttribute("jobPage", jobPage);

        return "member/job-bookmark";
    }

    // 개별 기업 북마크 삭제 API
    @DeleteMapping("/api/member/company-bookmark/{companyId}")
    @ResponseBody
    public ResponseEntity<Void> deleteCompanyBookmark(@PathVariable("companyId") Long companyId,
                                                      @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        companyBookmarkService.deleteCompanyBookmark(memberId, companyId);
        return ResponseEntity.ok().build();
    }

    // 복수 기업 북마크 삭제 API
    @PostMapping("/api/member/company-bookmark/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteCompanyBookmarks(@RequestBody java.util.List<Long> companyIds,
                                                       @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        companyBookmarkService.deleteCompanyBookmarks(memberId, companyIds);
        return ResponseEntity.ok().build();
    }

    // 개별 채용공고 북마크 삭제 API
    @DeleteMapping("/api/member/job-bookmark/{jobId}")
    @ResponseBody
    public ResponseEntity<String> deleteJobBookmark(@PathVariable("jobId") Long jobId,
                                                    @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        jobBookmarkService.removeJobBookmark(memberId, jobId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    // 복수 채용공고 북마크 삭제 API
    @PostMapping("/api/member/job-bookmark/delete")
    @ResponseBody
    public ResponseEntity<String> deleteJobBookmarks(@RequestBody java.util.List<Long> jobIds,
                                                     @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        jobBookmarkService.removeJobBookmarks(memberId, jobIds);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
