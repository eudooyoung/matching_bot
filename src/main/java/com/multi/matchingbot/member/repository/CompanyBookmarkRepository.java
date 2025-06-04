package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.entities.CompanyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyBookmarkRepository extends JpaRepository<CompanyBookmark, Long> {
    List<CompanyBookmark> findByCompanyId(Long companyId);
}
