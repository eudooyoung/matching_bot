package com.multi.matchingbot.community.controller;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityCommentDto;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.service.CommunityService;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final MemberService memberService;
    private final CompanyService companyService;

    @GetMapping("")
    public String redirectToList() {
        log.info("📌 redirectToList() 호출");
        return "redirect:/community/list";
    }

    @GetMapping("/list")
    public String list(@RequestParam(name = "categoryId", required = false, defaultValue = "") Long categoryId,
                       @RequestParam(name = "page",defaultValue = "0") int page,
                       @RequestParam(name = "size",defaultValue = "9") int size,
                       Model model,
                       Authentication authentication) {

        log.info("📌 list() 진입 - categoryId: {}, page: {}, size: {}", categoryId, page, size);


        List<CommunityCategory> categories = communityService.getAllCategories();
        Page<CommunityPostDto> postPage = communityService.getPagedPosts(categoryId, page, size);

        model.addAttribute("categories", categories);
        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent()); // 페이지 내용만 따로도 제공

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
        } else {
            model.addAttribute("currentUser", null);
        }

        return "community/community-list";
    }


    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("categories", communityService.getAllCategories());
        model.addAttribute("post", new CommunityPostDto());
        return "community/community-write";
    }

    // 글 등록 처리
    @PostMapping("/write")
    public String writePost(@ModelAttribute CommunityPostDto postDto,
                            Authentication authentication) {
        // JWT에서 추출된 사용자 정보
        String username = authentication.getName();

        // 또는 Custom UserDetails 사용하는 경우
        // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Member member = userDetails.getMember();

//        Member member = memberService.findByUsername(username);
//        communityService.createPost(postDto, member);
//        return "redirect:/community/list";
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_COMPANY"))) {
            Company company = companyService.findByEmail(username);
            communityService.createPostByCompany(postDto, company); // 별도 서비스 메서드 필요
        } else {
            Member member = memberService.findByUsername(username);
            communityService.createPost(postDto, member);
        }

        return "redirect:/community/list";
    }

//    @GetMapping("/{id}")
//    public String redirectToDetail(@PathVariable Long id) {
//        return "redirect:/community/detail/" + id;
//    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, Authentication authentication) {
        var post = communityService.getPostWithComments(id);
        model.addAttribute("post", CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());

        model.addAttribute("comment", post.getComments().stream()
                .map(CommunityCommentDto::fromEntity)
                .toList());

        if (authentication != null) {
            String email = authentication.getName();

            // 개인회원 or 기업회원 판단
            Long currentUserId;
            try {
                Member member = memberService.findByUsername(email);
                currentUserId = member.getId();
            } catch (EntityNotFoundException e) {
                Company company = companyService.findByEmail(email);
                currentUserId = company.getId();  // 주의: companyId임
            }

            model.addAttribute("currentUserId", currentUserId);
        } else {
            model.addAttribute("currentUserId", null);
        }

        return "community/community-detail";
    }


    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable(name = "id") Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        var post = communityService.getPostWithComments(id);
        String email = authentication.getName();

        boolean isOwner = false;
        try {
            Member member = memberService.findByUsername(email);
            isOwner = post.getMember() != null && post.getMember().getId().equals(member.getId());
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            isOwner = post.getCompany() != null && post.getCompany().getId().equals(company.getId());
        }

        if (!isOwner) return "redirect:/community/list";

        model.addAttribute("post", CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());
        return "community/community-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CommunityPostDto postDto,
                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        try {
            Member member = memberService.findByUsername(email);
            communityService.updatePost(id, postDto, member);
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            communityService.updatePostByCompany(id, postDto, company);
        }

        return "redirect:/community/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        try {
            Member member = memberService.findByUsername(email);
            communityService.deletePost(id, member);
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            communityService.deletePostByCompany(id, company);
        }

        return "redirect:/community/list";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") Long id,
                             @RequestParam("content") String content,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        try {
            Member member = memberService.findByUsername(email);
            communityService.addComment(id, content, member);
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            communityService.addCommentByCompany(id, content, company);
        }

        return "redirect:/community/detail/" + id;
    }

    @PostMapping("/comment/{id}/update")
    public String updateComment(@PathVariable("id") Long id,
                                @RequestParam("content") String content,
                                @RequestParam("postId") Long postId,
                                Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        try {
            Member member = memberService.findByUsername(email);
            communityService.updateComment(id, content, member);
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            communityService.updateCommentByCompany(id, content, company);
        }

        return "redirect:/community/detail/" + postId;
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        Long postId = communityService.getPostIdByCommentId(id);

        try {
            Member member = memberService.findByUsername(email);
            communityService.deleteComment(id, member.getId());
        } catch (EntityNotFoundException e) {
            Company company = companyService.findByEmail(email);
            communityService.deleteCommentByCompany(id, company.getId());
        }

        return "redirect:/community/detail/" + postId;
    }




//    @PostMapping("/comment/{id}/update")
//    public String updateComment(@PathVariable("id") Long id,
//                                @RequestParam("content") String content,
//                                @RequestParam("postId") Long postId,
//                                Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "redirect:/login";
//        }
//
//        Member member = memberService.findByUsername(authentication.getName());
//        communityService.updateComment(id, content, member);
//
//        return "redirect:/community/detail/" + postId;
//    }


}