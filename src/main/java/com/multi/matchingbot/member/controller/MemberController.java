package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.admin.mapper.MemberAdminMapper;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import com.multi.matchingbot.member.domain.dto.MemberUpdateDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.CompanyBookmarkService;
import com.multi.matchingbot.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private final MemberService memberService;
    private final MemberAdminMapper memberAdminMapper;
    private final CompanyBookmarkService companyBookmarkService;

    public MemberController(MemberService memberService, MemberAdminMapper memberAdminMapper, CompanyBookmarkService companyBookmarkService) {
        this.memberService = memberService;
        this.memberAdminMapper = memberAdminMapper;
        this.companyBookmarkService = companyBookmarkService;
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
}
