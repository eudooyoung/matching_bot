package com.multi.matchingbot.mapposting.repository;

import com.multi.matchingbot.mapposting.domain.MapPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapPostingRepository extends JpaRepository<MapPosting, Long> {
}
