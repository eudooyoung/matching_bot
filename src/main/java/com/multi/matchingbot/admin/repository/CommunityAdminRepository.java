package com.multi.matchingbot.admin.repository;

import com.multi.matchingbot.community.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommunityAdminRepository extends JpaRepository<CommunityPost, Long> {

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
