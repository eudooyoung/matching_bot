package com.multi.matchingbot.member.service;

import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.member.domain.entity.CompanyBookmark;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.repository.CompanyBookmarkRepository;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyBookmarkService {

    private final CompanyBookmarkRepository companyBookmarkRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    public Page<CompanyUpdateDto> getBookmarkedCompanys(Long memberId, Pageable pageable) {
        return companyBookmarkRepository.findCompanyUpdateDtosByMemberId(memberId, pageable);
    }

    @Transactional
    public void addBookmark(Long memberId, Long companyId) {
        // 이미 북마크가 있는지 확인
        if (companyBookmarkRepository.existsByMemberIdAndCompanyId(memberId, companyId)) {
            throw new IllegalStateException("이미 북마크된 기업입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("기업을 찾을 수 없습니다."));

        CompanyBookmark bookmark = CompanyBookmark.builder()
                .member(member)
                .company(company)
                .build();

        companyBookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long memberId, Long companyId) {
        CompanyBookmark bookmark = companyBookmarkRepository.findByMemberIdAndCompanyId(memberId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("북마크를 찾을 수 없습니다."));
        companyBookmarkRepository.delete(bookmark);
    }

    @Transactional
    public void removeBookmarks(Long memberId, List<Long> companyIds) {
        for (Long companyId : companyIds) {
            Optional<CompanyBookmark> bookmark = companyBookmarkRepository.findByMemberIdAndCompanyId(memberId, companyId);
            bookmark.ifPresent(companyBookmarkRepository::delete);
        }
    }

    @Transactional
    public boolean toggleBookmark(Long memberId, Long companyId) {
        Optional<CompanyBookmark> existingBookmark = companyBookmarkRepository.findByMemberIdAndCompanyId(memberId, companyId);

        if (existingBookmark.isPresent()) {
            // 북마크가 있으면 삭제
            companyBookmarkRepository.delete(existingBookmark.get());
            return false;
        } else {
            // 북마크가 없으면 추가
            addBookmark(memberId, companyId);
            return true;
        }
    }

    public boolean isBookmarked(Long memberId, Long companyId) {
        return companyBookmarkRepository.existsByMemberIdAndCompanyId(memberId, companyId);
    }
}
