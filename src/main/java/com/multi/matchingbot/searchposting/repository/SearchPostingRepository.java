package com.multi.matchingbot.searchposting.repository;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.mapposting.domain.MapPostingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SearchPostingRepository {
    List<Job> searchByFilters(String keyword, String title, String regionMain, String regionSub);
    Optional<Job> findById(Long id);
    Page<MapPostingDto> findAllMapPostings(Pageable pageable);
}
