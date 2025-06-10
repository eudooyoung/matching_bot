package com.multi.matchingbot.resume.repository;

import com.multi.matchingbot.member.domain.entity.ResumeViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeViewLogRepository extends JpaRepository<ResumeViewLog, Long> {

    List<ResumeViewLog> findByResume_Member_IdOrderByViewedAtDesc(Long memberId);
}