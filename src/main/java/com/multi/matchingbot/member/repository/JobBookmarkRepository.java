package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.member.domain.entity.JobBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobBookmarkRepository extends JpaRepository<JobBookmark, Long> {

    boolean existsByMemberIdAndJobId(Long memberId, Long jobId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM JobBookmark jb WHERE jb.member.id = :memberId AND jb.job.id = :jobId")
    void deleteByMemberIdAndJobId(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    @Query("SELECT jb.job.id FROM JobBookmark jb WHERE jb.member.id = :memberId")
    List<Long> findJobIdsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.multi.matchingbot.job.domain.dto.JobDto(j.id, j.company.id, j.occupation.id, j.skillKeywords, j.traitKeywords, " +
            "j.title, j.description, j.address, j.mainTask, j.requiredSkills, j.requiredTraits, " +
            "j.latitude, j.longitude, j.startDate, j.endDate, j.enrollEmail, j.notice, " +
            "j.createdBy, j.createdAt, j.updatedBy, j.updatedAt, j.company.name) " +
            "FROM JobBookmark jb " +
            "JOIN jb.job j " +
            "WHERE jb.member.id = :memberId")
    Page<JobDto> findJobDtosByMemberId(@Param("memberId") Long memberId, Pageable pageable);


}