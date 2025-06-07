package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entity.Resume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "occupation")
    Optional<Resume> findWithOccupationById(Long id);
}
