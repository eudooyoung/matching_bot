package com.multi.matchingbot.community.repository;

import com.multi.matchingbot.community.domain.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByCategoryId(Long categoryId);
    @Query("SELECT p FROM CommunityPost p " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.company " +
            "LEFT JOIN FETCH p.member " +  // 멤버도 Lazy 로딩이면 같이 fetch
            "WHERE p.id = :id")
    Optional<CommunityPost> findByIdWithComments(@Param("id") Long id); // ✅ 파라미터 지정
    Page<CommunityPost> findByCategoryId(Long categoryId, Pageable pageable);
    Page<CommunityPost> findAll(Pageable pageable);


}
