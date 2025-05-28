package com.multi.matchingbot.community.repository;

import com.multi.matchingbot.community.domain.CommunityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {
}
