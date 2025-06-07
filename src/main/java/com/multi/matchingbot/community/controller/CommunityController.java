package com.multi.matchingbot.community.controller;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityCommentDto;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.service.CommunityService;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
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

        model.addAttribute("comment", post.getComments().stream()
                .map(CommunityCommentDto::fromEntity)
                .toList());

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

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable(name = "id") Long postId,
                             @RequestParam("content") String content,
                             Authentication authentication) {

        // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // 로그인한 사용자 정보로부터 Member 객체 조회
        Member member = memberService.findByUsername(authentication.getName());

        // 댓글 저장 서비스 호출
        communityService.addComment(postId, content, member);

        // 댓글 작성 후 해당 게시글 상세 페이지로 리다이렉트
        return "redirect:/community/detail/" + postId;
    }

    @PostMapping("/comment/edit")
    public String updateComment(@RequestParam Long id,
                                @RequestParam String content,
                                Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(authentication.getName());
        communityService.updateComment(id, content, member);

        Long postId = communityService.getPostIdByCommentId(id);
        return "redirect:/community/detail/" + postId;
    }


    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable(name = "id") Long commentId,
                                Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(authentication.getName());
        Long memberId = member.getId();

        // ✅ 댓글 삭제 전, 해당 댓글이 달린 게시글 ID 조회
        Long postId = communityService.getPostIdByCommentId(commentId);

        communityService.deleteComment(commentId, memberId);
        log.info("🔁 댓글 삭제 후 이동할 게시글 ID: {}", postId);

        return "redirect:/community/detail/" + postId;

    }




    @PostMapping("/comment/{id}/update")
    public String updateComment(@PathVariable("id") Long id,
                                @RequestParam("content") String content,
                                @RequestParam("postId") Long postId,
                                Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(authentication.getName());
        communityService.updateComment(id, content, member);

        return "redirect:/community/detail/" + postId;
    }


}