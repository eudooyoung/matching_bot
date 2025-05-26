package com.multi.matchingbot.searchposting.repository;

import com.multi.matchingbot.mapposting.domain.MapPosting;
import com.multi.matchingbot.mapposting.domain.MapPostingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SearchPostingRepository {
    List<MapPosting> searchByFilters(String keyword, String title, String skill, String region);
    Optional<MapPosting> findById(Long id);
    Page<MapPostingDto> findAllMapPostings(Pageable pageable);
}
