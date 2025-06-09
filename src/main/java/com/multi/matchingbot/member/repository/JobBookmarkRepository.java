package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entity.JobBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobBookmarkRepository extends JpaRepository<JobBookmark, Long> {

    boolean existsByMemberIdAndJobId(Long memberId, Long jobId);

    @Modifying
    @Query("DELETE FROM JobBookmark jb WHERE jb.member.id = :memberId AND jb.job.id = :jobId")
    void deleteByMemberIdAndJobId(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    @Query("SELECT jb.job.id FROM JobBookmark jb WHERE jb.member.id = :memberId")
    List<Long> findJobIdsByMemberId(@Param("memberId") Long memberId);
}