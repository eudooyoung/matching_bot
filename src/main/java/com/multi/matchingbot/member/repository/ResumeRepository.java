package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.entity.Resume;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "occupation")
    Optional<Resume> findWithOccupationById(Long id);

    @Query("SELECT r FROM Resume r JOIN FETCH r.occupation WHERE r.id = :id AND r.member = :member")
    Optional<Resume> findWithOccupationByIdAndMember(@Param("id") Long id, @Param("member") Member member);

    Optional<Resume> findByIdAndMember(@NotNull Long id, Member member);
}
