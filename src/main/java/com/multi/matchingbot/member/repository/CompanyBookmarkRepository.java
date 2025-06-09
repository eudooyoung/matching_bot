package com.multi.matchingbot.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.member.domain.entity.CompanyBookmark;

@Repository
public interface CompanyBookmarkRepository extends JpaRepository<CompanyBookmark, Long> {

    List<CompanyBookmark> findByCompanyId(Long companyId);

    @Query("SELECT new com.multi.matchingbot.company.domain.CompanyUpdateDto(c.id, c.name, c.phone, c.email) " +
            "FROM CompanyBookmark cb " +
            "JOIN cb.company c " +
            "WHERE cb.member.id = :memberId")
    Page<CompanyUpdateDto> findCompanyUpdateDtosByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    // memberId와 companyId로 북마크 존재 여부 확인
    boolean existsByMemberIdAndCompanyId(Long memberId, Long companyId);

    // memberId와 companyId로 북마크 조회
    Optional<CompanyBookmark> findByMemberIdAndCompanyId(Long memberId, Long companyId);
}
