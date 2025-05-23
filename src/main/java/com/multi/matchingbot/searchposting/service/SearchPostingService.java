//package com.multi.matchingbot.searchposting;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class SearchPostingService {
//    private SearchPostingRepository searchPostingRepository;
//
//    public List<SearchPostingDto> searchJobs(String keyword, String title, String skill, String region) {
//        searchPostingRepository.searchbyFilters(keyword,title,skill,region);
//        return searchJobs().stream()
//                .map(SearchPostingDto::fromEntity)
//                .collect(Collectors.toList());
//
//    }
//}
