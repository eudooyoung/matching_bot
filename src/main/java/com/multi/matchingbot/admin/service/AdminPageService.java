package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.common.domain.dto.PagedResult;
import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.MemberAdminViewDto;
import com.multi.matchingbot.member.domain.dtos.ResumeAdminViewDto;
import com.multi.matchingbot.member.domain.entities.Member;
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

    public PagedResult<MemberAdminViewDto> members(SearchCondition cond) {
        Yn status = (cond.getStatus() != null && !cond.getStatus().isBlank())
                ? Yn.valueOf(cond.getStatus())
                : null;

        Pageable pageable = cond.toPageable();
        Page<Member> members = memberRepository.searchWithCondition(cond.getKeyword(), status, pageable);
        Page<MemberAdminViewDto> pageResult = members.map(memberMapper::toMemberAdminView);
        return new PagedResult<>(pageResult);
    }

    public PagedResult<ResumeAdminViewDto> resumes(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ResumeAdminViewDto> pageResult = resumeRepository.findAll(pageable).map(resumeMapper::toResumeAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminViewDto> companies(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminViewDto> jobs(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminViewDto> communitiy(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

    public PagedResult<MemberAdminViewDto> attachedItems(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MemberAdminViewDto> pageResult = memberRepository.findAll(pageable).map(memberMapper::toMemberAdminView);

        return new PagedResult<>(pageResult);
    }

}
