//package com.multi.matchingbot.searchposting.service;
//
//import com.multi.matchingbot.searchposting.domain.SearchPostingDto;
//import com.multi.matchingbot.searchposting.repository.SearchPostingRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class SearchPostingService {
//
//    private final SearchPostingRepository searchPostingRepository;
//
//    public List<SearchPostingDto> searchJobs(String keyword, String title, String skill, String region) {
//        List<SearchPosting> postings = searchPostingRepository.searchbyFilters(keyword, title, skill, region);
//
//        return postings.stream()
//                .map(SearchPostingDto::fromEntity)
//                .collect(Collectors.toList());
//    }
//}
