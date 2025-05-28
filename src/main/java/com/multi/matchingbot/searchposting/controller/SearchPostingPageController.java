package com.multi.matchingbot.searchposting.controller;

import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
import com.multi.matchingbot.searchposting.service.SearchPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchPostingPageController {

    private final SearchPostingService searchPostingService;

    @GetMapping("/search-page")
    public String searchPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String region,
            Model model
    ) {
        List<SearchPostingDto> jobList = searchPostingService.searchJobs(keyword, title, skill, region);
        model.addAttribute("jobList", jobList);
        return "search/search_result";
    }

    // SearchPostingPageController.java 내부
    @GetMapping("/jobs/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        SearchPostingDto job = searchPostingService.findById(id); // 아래 서비스도 함께 추가
        model.addAttribute("job", job);
        return "search/search_detail"; // templates/search/search_detail.html
    }


}
