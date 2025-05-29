package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.CompanyAdminView;
import com.multi.matchingbot.admin.domain.MemberAdminView;
import com.multi.matchingbot.admin.domain.ResumeAdminView;
import com.multi.matchingbot.admin.mapper.CompanyAdminMapper;
import com.multi.matchingbot.admin.mapper.MemberAdminMapper;
import com.multi.matchingbot.admin.mapper.ResumeAdminMapper;
import com.multi.matchingbot.admin.repository.CompanyAdminRepository;
import com.multi.matchingbot.admin.repository.MemberAdminRepository;
import com.multi.matchingbot.admin.repository.ResumeAdminRepository;
import com.multi.matchingbot.common.domain.dto.PagedResult;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.domain.entities.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminPageService {

    private final MemberAdminRepository memberAdminRepository;
    private final MemberAdminMapper memberAdminMapper;
    private final CompanyAdminMapper companyAdminMapper;
    private final CompanyAdminRepository companyAdminRepository;
    private final ResumeAdminRepository resumeAdminRepository;
    private final ResumeAdminMapper resumeAdminMapper;

    public PagedResult<MemberAdminView> members(SearchCondition condition) {
        Yn status = getStatus(condition.getStatus());

        Pageable pageable = condition.toPageable();
        Page<Member> members = memberAdminRepository.searchWithCondition(condition.getKeyword(), status, pageable);
        Page<MemberAdminView> pageResult = members.map(memberAdminMapper::toMemberAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<CompanyAdminView> companies(SearchCondition condition) {
        Yn status = getStatus(condition.getStatus());
        Yn reportStatus = getStatus(condition.getReportStatus());

        Pageable pageable = condition.toPageable();
        Page<Company> companies = companyAdminRepository.searchWithCondition(condition.getKeyword(), status, reportStatus, pageable);
        Page<CompanyAdminView> pageResult = companies.map(companyAdminMapper::toCompanyAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<ResumeAdminView> resumes(SearchCondition condition) {
        Yn keywordsStatus = getStatus(condition.getKeywordsStatus());

        Pageable pageable = condition.toPageable();
        Page<Resume> resumes = resumeAdminRepository.searchWithCondition(condition.getKeyword(), keywordsStatus, pageable);
        Page<ResumeAdminView> pageResult = resumes.map(resumeAdminMapper::toResumeAdminView);
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
