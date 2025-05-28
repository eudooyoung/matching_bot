package com.multi.matchingbot.community.controller;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("")
    public String redirectToList() {
        log.info("📌 redirectToList() 호출");
        return "redirect:/community/list";
    }



    @GetMapping("/list")
    public String list(@RequestParam(required = false) Long categoryId, Model model) {
        log.info("📌 list() 진입 - categoryId: {}", categoryId);

        List<CommunityCategory> categories = communityService.getAllCategories();
        List<CommunityPostDto> postList = communityService.getPostsByCategory(categoryId);

        model.addAttribute("categories", categories);
        model.addAttribute("postList", postList);

        return "community/community-list";
    }


    // 실제 View 반환용 메서드 (디버깅 성공 후 사용)
//    @GetMapping("/list-view")
//    public String listView(@RequestParam(required = false) Long categoryId, Model model) {
//        try {
//            log.info("📌 listView() 진입 - categoryId: {}", categoryId);
//
//            List<CommunityCategory> categories = communityService.getAllCategories();
//            List<CommunityPostDto> postList = communityService.getPostsByCategory(categoryId);
//
//            model.addAttribute("categories", categories);
//            model.addAttribute("postList", postList);
//
//            return "community/community-list";  // 템플릿 파일 경로
//
//        } catch (Exception e) {
//            log.error("📌 listView() 에러 발생: ", e);
//            model.addAttribute("errorMessage", "게시글 목록을 불러오는 중 오류가 발생했습니다.");
//            return "error/error";
//        }
 //   }
}