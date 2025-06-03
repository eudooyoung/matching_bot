package com.multi.matchingbot.community.repository;

import com.multi.matchingbot.community.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByCategoryId(Long categoryId);
    @Query("SELECT p FROM CommunityPost p LEFT JOIN FETCH p.comments WHERE p.id = :postId")
    Optional<CommunityPost> findByIdWithComments(@Param("postId") Long postId);

}
