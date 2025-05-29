package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.common.domain.dto.PagedResult;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.domain.CompanyAdminView;
import com.multi.matchingbot.company.mapper.CompanyAdminMapper;
import com.multi.matchingbot.member.domain.dtos.MemberAdminView;
import com.multi.matchingbot.member.domain.dtos.ResumeAdminView;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.member.mapper.MemberMapper;
import com.multi.matchingbot.member.mapper.ResumeMapper;
import com.multi.matchingbot.member.repository.MemberRepository;
import com.multi.matchingbot.member.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminPageService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CompanyAdminMapper companyAdminMapper;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;

    public PagedResult<MemberAdminView> members(SearchCondition condition) {
        Yn status = getStatus(condition.getStatus());

        Pageable pageable = condition.toPageable();
        Page<Member> members = memberRepository.searchWithCondition(condition.getKeyword(), status, pageable);
        Page<MemberAdminView> pageResult = members.map(memberMapper::toMemberAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<CompanyAdminView> companies(SearchCondition condition) {
        Yn status = getStatus(condition.getStatus());
        Yn reportStatus = getStatus(condition.getReportStatus());

        Pageable pageable = condition.toPageable();
        Page<Company> companies = companyRepository.searchWithCondition(condition.getKeyword(), status, reportStatus, pageable);
        Page<CompanyAdminView> pageResult = companies.map(companyAdminMapper::toCompanyAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<ResumeAdminView> resumes(SearchCondition condition) {
        Yn keywordsStatus = getStatus(condition.getKeywordsStatus());

        Pageable pageable = condition.toPageable();
        Page<Resume> resumes = resumeRepository.searchWithCondition(condition.getKeyword(), keywordsStatus, pageable);
        Page<ResumeAdminView> pageResult = resumes.map(resumeMapper::toResumeAdminView);
        return new PagedResult<>(pageResult);
    }

    private static Yn getStatus(String condition) {
        Yn status = (condition != null && !condition.isBlank())
                ? Yn.valueOf(condition)
                : null;
        return status;
    }

//    public PagedResult<MemberAdminView> jobs(int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);
//
//        return new PagedResult<>(pageResult);
//    }
//
//    public PagedResult<MemberAdminView> communitiy(int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);
//
//        return new PagedResult<>(pageResult);
//    }
//
//    public PagedResult<MemberAdminView> attachedItems(int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);
//
//        return new PagedResult<>(pageResult);
//    }

}
