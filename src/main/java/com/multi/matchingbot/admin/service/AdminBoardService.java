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
import com.multi.matchingbot.admin.domain.AdminPagedResult;
import com.multi.matchingbot.admin.domain.AdminSearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminBoardService {

    private final MemberAdminRepository memberAdminRepository;
    private final MemberAdminMapper memberAdminMapper;
    private final CompanyAdminMapper companyAdminMapper;
    private final CompanyAdminRepository companyAdminRepository;
    private final ResumeAdminRepository resumeAdminRepository;
    private final ResumeAdminMapper resumeAdminMapper;

    public AdminPagedResult<MemberAdminView> members(AdminSearchCondition condition) {
        Yn status = getStatus(condition.getStatus());

        Pageable pageable = condition.toPageable();
        Page<Member> members = memberAdminRepository.searchWithCondition(condition.getKeyword(), status, pageable);
        Page<MemberAdminView> pageResult = members.map(memberAdminMapper::toMemberAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<CompanyAdminView> companies(AdminSearchCondition condition) {
        Yn status = getStatus(condition.getStatus());
        Yn reportStatus = getStatus(condition.getReportStatus());

        Pageable pageable = condition.toPageable();
        Page<Company> companies = companyAdminRepository.searchWithCondition(condition.getKeyword(), status, reportStatus, pageable);
        Page<CompanyAdminView> pageResult = companies.map(companyAdminMapper::toCompanyAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<ResumeAdminView> resumes(AdminSearchCondition condition) {
        Yn keywordsStatus = getStatus(condition.getKeywordsStatus());

        Pageable pageable = condition.toPageable();
        Page<Resume> resumes = resumeAdminRepository.searchWithCondition(condition.getKeyword(), keywordsStatus, pageable);
        Page<ResumeAdminView> pageResult = resumes.map(resumeAdminMapper::toResumeAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    private static Yn getStatus(String condition) {
        Yn status = (condition != null && !condition.isBlank())
                ? Yn.valueOf(condition)
                : null;
        return status;
    }

}
