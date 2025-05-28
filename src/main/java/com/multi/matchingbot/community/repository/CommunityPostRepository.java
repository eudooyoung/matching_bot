package com.multi.matchingbot.community.repository;

import com.multi.matchingbot.community.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByCategoryId(Long categoryId);
}
