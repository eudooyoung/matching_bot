package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.admin.domain.AdminPagedResult;
import com.multi.matchingbot.admin.domain.AdminSearchCondition;
import com.multi.matchingbot.admin.domain.enums.EndStatus;
import com.multi.matchingbot.admin.domain.view.*;
import com.multi.matchingbot.admin.mapper.*;
import com.multi.matchingbot.admin.repository.*;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.community.domain.CommunityPost;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.resume.domain.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class AdminBoardService {

    private final MemberAdminRepository memberAdminRepository;
    private final CompanyAdminRepository companyAdminRepository;
    private final ResumeAdminRepository resumeAdminRepository;
    private final JobAdminRepository jobAdminRepository;
    private final CommunityAdminRepository communityAdminRepository;

    private final MemberAdminMapper memberAdminMapper;
    private final CompanyAdminMapper companyAdminMapper;
    private final ResumeAdminMapper resumeAdminMapper;
    private final JobAdminMapper jobAdminMapper;
    private final CommunityAdminMapper communityAdminMapper;

    public AdminPagedResult<MemberAdminView> members(AdminSearchCondition condition) {
        Yn status = getStatus(condition.getStatus());

        Pageable pageable = condition.toPageable();
        Page<Member> members = memberAdminRepository.searchWithCondition(
                condition.getKeyword(),
                status,
                pageable
        );
        Page<MemberAdminView> pageResult = members.map(memberAdminMapper::toMemberAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<CompanyAdminView> companies(AdminSearchCondition condition) {
        Yn status = getStatus(condition.getStatus());
        Yn reportStatus = getStatus(condition.getReportStatus());

        Pageable pageable = condition.toPageable();
        Page<Company> companies = companyAdminRepository.searchWithCondition(
                condition.getKeyword(),
                status,
                reportStatus,
                pageable
        );
        Page<CompanyAdminView> pageResult = companies.map(companyAdminMapper::toCompanyAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<ResumeAdminView> resumes(AdminSearchCondition condition) {
//        Yn keywordsStatus = getStatus(condition.getKeywordsStatus());

        Pageable pageable = condition.toPageable();
        Page<Resume> resumes = resumeAdminRepository.searchWithCondition(
                condition.getKeyword(),
                condition.getCareerType(),
                pageable
        );
        Page<ResumeAdminView> pageResult = resumes.map(resumeAdminMapper::toResumeAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<JobAdminView> jobs(AdminSearchCondition condition) {
        EndStatus endStatus = condition.getEndStatus() != null ? condition.getEndStatus() : EndStatus.ALL;
        LocalDate today = LocalDate.now();

        Pageable pageable = condition.toPageable();

        Page<Job> jobs = jobAdminRepository.searchWithCondition(
                condition.getKeyword(),
                endStatus,
                today,
                pageable
        );
        Page<JobAdminView> pageResult = jobs.map(jobAdminMapper::toJobAdminView);
        return new AdminPagedResult<>(pageResult);
    }

    public AdminPagedResult<CommunityAdminView> posts(AdminSearchCondition condition) {
        Pageable pageable = condition.toPageable();

        Page<CommunityPost> page = communityAdminRepository
                .searchWithCondition(
                condition.getKeyword(),
                condition.getCategoryId(),
                condition.getWriterType(),  // ✅ 추가
                pageable
        );

        Page<CommunityAdminView> viewPage = page.map(communityAdminMapper::toDto);
        return new AdminPagedResult<>(viewPage);
    }

    private static Yn getStatus(String condition) {
        Yn status = (condition != null && !condition.isBlank())
                ? Yn.valueOf(condition)
                : null;
        return status;
    }

}
