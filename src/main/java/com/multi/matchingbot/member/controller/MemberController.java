package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.admin.mapper.MemberAdminMapper;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.admin.domain.MemberAdminView;
import com.multi.matchingbot.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberAdminMapper memberAdminMapper;

    public MemberController(MemberService memberService, MemberAdminMapper memberAdminMapper) {
        this.memberService = memberService;
        this.memberAdminMapper = memberAdminMapper;
    }

    // 구직자 마이페이지 진입
    @GetMapping("/mypage")
    public String showMypage(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getId();

        MemberAdminView memberView = memberAdminMapper.toMemberAdminView(
                memberService.getMemberById(memberId)
        );

        model.addAttribute("member", memberView);
        model.addAttribute("role", userDetails.getRole().name());
        return "member/member-mypage";
    }

    //     추후 개인정보 수정 페이지로 이동할 때 사용
    @GetMapping("/profile_edit")
    public String editProfileForm(Model model, @AuthenticationPrincipal MBotUserDetails userDetails) {
        Long memberId = userDetails.getId();
        MemberAdminView memberView = memberAdminMapper.toMemberAdminView(
                memberService.getMemberById(memberId)
        );

        model.addAttribute("member", memberView);
        return "member/edit-profile";
    }
}
