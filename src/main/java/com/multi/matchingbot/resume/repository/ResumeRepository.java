package com.multi.matchingbot.resume.repository;

import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.entity.Resume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {
    List<Resume> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "occupation")
    Optional<Resume> findWithOccupationById(Long id);

    @Query("SELECT r FROM Resume r JOIN FETCH r.occupation WHERE r.id = :id AND r.member = :member")
    Optional<Resume> findWithOccupationByIdAndMember(@Param("id") Long id, @Param("member") Member member);

    Optional<Resume> findByIdAndMember(Long id, Member member);


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


    @Query("""
                SELECT r FROM Resume r
                LEFT JOIN FETCH r.member
                LEFT JOIN FETCH r.occupation
                LEFT JOIN FETCH r.careers
                WHERE r.id = :id
            """)
    Optional<Resume> findByIdWithAll(@Param("id") Long id);
}
