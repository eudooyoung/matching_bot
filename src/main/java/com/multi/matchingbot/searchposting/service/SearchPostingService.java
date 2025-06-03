package com.multi.matchingbot.searchposting.service;


import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
import com.multi.matchingbot.searchposting.repository.SearchPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchPostingService {

    private final SearchPostingRepository searchPostingRepository;

    public List<SearchPostingDto> searchJobs(String keyword, String title, String regionMain, String regionSub) {
        List<Job> postings = searchPostingRepository.searchByFilters(keyword, title, regionMain, regionSub);
        return postings.stream()
                .map(SearchPostingDto::fromEntity)
                .collect(Collectors.toList());
    }


    public SearchPostingDto findById(Long id) {
        return searchPostingRepository.findById(id)
                .map(SearchPostingDto::fromEntity)
                .orElse(null);
    }


}
