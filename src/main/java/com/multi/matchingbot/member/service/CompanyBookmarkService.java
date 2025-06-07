package com.multi.matchingbot.member.service;

import com.multi.matchingbot.company.domain.CompanyUpdateDto;
import com.multi.matchingbot.member.repository.CompanyBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyBookmarkService {

    private final CompanyBookmarkRepository companyBookmarkRepository;

    public Page<CompanyUpdateDto> getBookmarkedCompanys(Long memberId, Pageable pageable) {
        return companyBookmarkRepository.findCompanyUpdateDtosByMemberId(memberId, pageable);
    }
}
