package com.multi.matchingbot.member.service;

import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyUpdateDto;
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
    public void deleteCompanyBookmark(Long memberId, Long companyId) {
        companyBookmarkRepository.deleteByMemberIdAndCompanyId(memberId, companyId);
    }

    @Transactional
    public void deleteCompanyBookmarks(Long memberId, List<Long> companyIds) {
        companyBookmarkRepository.deleteByMemberIdAndCompanyIdIn(memberId, companyIds);
    }

    @Transactional
    public void addCompanyBookmark(Long memberId, Long companyId) {
        if (!isBookmarked(memberId, companyId)) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("기업을 찾을 수 없습니다."));

            CompanyBookmark companyBookmark = CompanyBookmark.builder()
                    .member(member)
                    .company(company)
                    .build();
            companyBookmarkRepository.save(companyBookmark);
        }
    }

    public boolean isBookmarked(Long memberId, Long companyId) {
        return companyBookmarkRepository.existsByMemberIdAndCompanyId(memberId, companyId);
    }
}
