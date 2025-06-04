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

    @GetMapping("/job")
    public List<SearchPostingDto> searchJobs(
            @RequestParam(required = false) String jobGroup,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String jobRole,
            @RequestParam(required = false) String regionMain,
            @RequestParam(required = false) String regionSub
    ) {
        return searchPostingService.searchJobs(jobGroup, jobType, jobRole, regionMain, regionSub);
    }

}
