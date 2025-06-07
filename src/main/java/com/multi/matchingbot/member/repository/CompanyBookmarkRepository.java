package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.member.domain.entity.CompanyBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.multi.matchingbot.member.domain.entity.CompanyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyBookmarkRepository extends JpaRepository<CompanyBookmark, Long> {

    List<CompanyBookmark> findByCompanyId(Long companyId);

    @Query("SELECT new com.multi.matchingbot.company.domain.CompanyUpdateDto(c.id, c.name, c.phone, c.email) " +
            "FROM CompanyBookmark cb " +
            "JOIN cb.company c " +
            "WHERE cb.member.id = :memberId")
    Page<CompanyUpdateDto> findCompanyUpdateDtosByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
