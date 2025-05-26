package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.member.mapper.MemberMapper;
import com.multi.matchingbot.member.MemberRepository;
import com.multi.matchingbot.member.domain.MemberAdminViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @GetMapping({"/", "/main", ""})
    public String mainPage(Model model, @AuthenticationPrincipal MBotUserDetails user) {
        if (user != null) {
            log.info("userType: {}", user.getRole());
            model.addAttribute("role", user.getRole());
        } else {
            model.addAttribute("role", null);
            log.info("비회원 접근");
        }
        return "main/main";
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/members")
    public void members(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> members = memberRepository.findByRoleNot(Role.ADMIN, pageable).map(memberMapper::toMemberAdminView);
        int totalPages = members.getTotalPages();
        int currentPage = members.getNumber();

        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().toList(); // 0부터 시작

        model.addAttribute("members", members);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", currentPage);
    }

    @GetMapping("/resumes")
    public void resumes(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> resumes = resumeRepository.findByRoleNot(Role.ADMIN, pageable).map(memberMapper::toMemberAdminView);
        int totalPages = members.getTotalPages();
        int currentPage = members.getNumber();

        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().toList(); // 0부터 시작

        model.addAttribute("members", members);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", currentPage);
    }
}
