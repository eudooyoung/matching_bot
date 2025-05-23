//package com.multi.matchingbot.searchposting.controller;
//
//
//import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
//import com.multi.matchingbot.searchposting.service.SearchPostingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/search")
//@RequiredArgsConstructor
//public class SearchPostingController {
//    private final SearchPostingService searchPostingService;
//
//    @GetMapping
//    public ResponseEntity<List<SearchPostingDto>> searchJobs(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String skill,
//            @RequestParam(required = false) String region){
//
//        List<SearchPostingDto> results = searchPostingService.searchJobs(keyword, title, skill, region);
//        return ResponseEntity.ok(results);
//    }
//}
//
