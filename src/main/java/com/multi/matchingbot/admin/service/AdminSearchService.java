package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.common.domain.dto.SearchCondition;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.member.domain.dtos.MemberAdminViewDto;
import com.multi.matchingbot.member.domain.entities.Member;
import com.multi.matchingbot.member.mapper.MemberMapper;
import com.multi.matchingbot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSearchService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public Page<MemberAdminViewDto> memberSearch(SearchCondition cond, Pageable pageable) {
        Yn status = (cond.getStatus() != null && !cond.getStatus().isBlank())
                ? Yn.valueOf(cond.getStatus())
                :null;

        Page<Member> members = memberRepository.searchWithCondition(cond.getKeyword(), status, pageable);
        return members.map(memberMapper::toMemberAdminView);
    }
}
