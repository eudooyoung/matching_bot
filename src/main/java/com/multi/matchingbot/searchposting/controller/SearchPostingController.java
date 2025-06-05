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
            @RequestParam(name = "jobGroup", required = false) String jobGroup,
            @RequestParam(name = "jobType", required = false) String jobType,
            @RequestParam(name = "jobRole", required = false) String jobRole,
            @RequestParam(name = "regionMain", required = false) String regionMain,
            @RequestParam(name = "regionSub", required = false) String regionSub,
            @RequestParam(name = "companyName", required = false) String companyName
    ) {
        return searchPostingService.searchJobs(companyName,jobGroup, jobType, jobRole, regionMain, regionSub);
    }

}
