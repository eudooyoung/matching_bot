package com.multi.matchingbot.community.controller;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.service.CommunityService;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("")
    public String redirectToList() {
        log.info("📌 redirectToList() 호출");
        return "redirect:/community/list";
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Long categoryId, Model model, Authentication authentication) {
        log.info("📌 list() 진입 - categoryId: {}", categoryId);

        List<CommunityCategory> categories = communityService.getAllCategories();
        List<CommunityPostDto> postList = communityService.getPostsByCategory(categoryId);

        model.addAttribute("categories", categories);
        model.addAttribute("postList", postList);

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

        Member member = memberService.findByUsername(username);
        communityService.createPost(postDto, member);
        return "redirect:/community/list";
    }

//    @GetMapping("/{id}")
//    public String redirectToDetail(@PathVariable Long id) {
//        return "redirect:/community/detail/" + id;
//    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model,Authentication authentication) {
        var post = communityService.getPostWithComments(id);
        model.addAttribute("post",CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());

        if (authentication != null) {
            Member member = memberService.findByUsername(authentication.getName());
            model.addAttribute("currentUserId", member.getId());
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
        Member member = memberService.findByUsername(authentication.getName());

        if (!post.getMember().getId().equals(member.getId())) {
            return "redirect:/community/list"; // 본인 게시글 아니면 차단
        }

        model.addAttribute("post", CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());

        return "community/community-edit"; // ← HTML 템플릿 이름
    }


    @PostMapping("/edit/{id}")
    public String update(@PathVariable(name = "id") Long id, @ModelAttribute CommunityPostDto postDto,
                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }



        Member member = memberService.findByUsername(authentication.getName());
        communityService.updatePost(id, postDto, member);
        return "redirect:/community/detail/" + id;
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, Authentication authentication) {
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }


        Member member = memberService.findByUsername(authentication.getName());
        communityService.deletePost(id, member);
        return "redirect:/community/list";
    }

}