package com.multi.matchingbot.admin.service;

import com.multi.matchingbot.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final MemberRepository memberRepository;


}
