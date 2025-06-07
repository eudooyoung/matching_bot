package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.resume.repository.ResumeRepositoryCustom;
import com.multi.matchingbot.member.domain.entity.Resume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {
    List<Resume> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "occupation")
    Optional<Resume> findWithOccupationById(Long id);


//    // ✅ 조건별 검색 쿼리
//    @Query("""
//    SELECT r FROM Resume r
//    JOIN r.member m
//    JOIN r.occupation o
//    WHERE (:jobGroup IS NULL OR o.jobGroupName = :jobGroup)
//      AND (:jobType IS NULL OR o.jobTypeName = :jobType)
//      AND (:jobRole IS NULL OR o.jobRoleName = :jobRole)
//      AND (:careerType IS NULL OR r.careerType = :careerType)
//      AND (:companyName IS NULL OR m.name LIKE %:companyName%)
//""")
//    List<Resume> findByFilters(@Param("jobGroup") String jobGroup,
//                               @Param("jobType") String jobType,
//                               @Param("jobRole") String jobRole,
//                               @Param("careerType") String careerType,
//                               @Param("companyName") String companyName);


}
