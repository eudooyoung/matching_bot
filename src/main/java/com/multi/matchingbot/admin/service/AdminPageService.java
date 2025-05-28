package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.common.domain.dto.PagedResult;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminPageService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;

    public PagedResult<MemberAdminView> members(SearchCondition cond) {
        Yn status = (cond.getStatus() != null && !cond.getStatus().isBlank())
                ? Yn.valueOf(cond.getStatus())
                : null;

        Pageable pageable = cond.toPageable();
        Page<Member> members = memberRepository.searchWithCondition(cond.getKeyword(), status, pageable);
        Page<MemberAdminView> pageResult = members.map(memberMapper::toMemberAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<ResumeAdminView> resumes(SearchCondition cond) {
        Yn status = (cond.getStatus() != null && !cond.getStatus().isBlank())
                ? Yn.valueOf(cond.getStatus())
                : null;

        Pageable pageable = cond.toPageable();
        Page<Resume> resumes = resumeRepository.searchWithCondition(cond.getKeyword(), status, pageable);
        Page<ResumeAdminView> pageResult = resumes.map(resumeMapper::toResumeAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminView> companies(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminView> jobs(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminView> communitiy(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminView> attachedItems(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminView> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

}
