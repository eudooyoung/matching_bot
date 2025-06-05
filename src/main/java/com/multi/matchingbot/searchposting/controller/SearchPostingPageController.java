package com.multi.matchingbot.searchposting.controller;

import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
import com.multi.matchingbot.searchposting.service.SearchPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchPostingPageController {

    private final SearchPostingService searchPostingService;

    @GetMapping("/search-page")
    public String searchPage(@RequestParam(name="jobGroup", required = false) String jobGroup,
                             @RequestParam(name="jobType",required = false) String jobType,
                             @RequestParam(name="jobRole",required = false) String jobRole,
                             @RequestParam(name="regionMain",required = false) String regionMain,
                             @RequestParam(name="regionSub",required = false) String regionSub,
                             @RequestParam(name="companyName",required = false) String companyName,

                             Model model) {

        List<SearchPostingDto> jobList = searchPostingService.searchJobs(jobGroup, jobType, jobRole, regionMain, regionSub,companyName);

        model.addAttribute("jobList", jobList);

        return "search/search-result"; // 검색 결과 뷰
    }



//    @GetMapping("/job/{Postid}")
//    public String viewDetail(@PathVariable(name = "Postid") Long id, Model model) {
//        SearchPostingDto job = searchPostingService.findById(id); // 아래 서비스도 함께 추가
//        model.addAttribute("job", job);
//        return "search/search-detail"; // templates/search/search-detail.html
//    }


}
