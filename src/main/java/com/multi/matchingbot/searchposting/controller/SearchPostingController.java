package com.multi.matchingbot.searchposting.controller;

import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
import com.multi.matchingbot.searchposting.service.SearchPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchPostingController {

    private final SearchPostingService searchPostingService;

    @GetMapping("/jobs")
    public List<SearchPostingDto> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String region
    ) {
        return searchPostingService.searchJobs(keyword, title, skill, region);
    }
}
