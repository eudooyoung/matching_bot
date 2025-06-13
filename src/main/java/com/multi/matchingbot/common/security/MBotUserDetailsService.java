package com.multi.matchingbot.common.security;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.error.InvalidRoleException;
import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.member.repository.MemberRepository;
import com.multi.matchingbot.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MBotUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("이 서비스는 loadUserByType(...)만 지원합니다.");
    }

    public UserDetails loadUserByTypeAndEmail(Role role, String email) {
        log.warn("▶ loadByType 호출 - email: {}, role: {}", email, role);

        switch (role) {
            case MEMBER:
                return memberRepository.findByEmail(email)
                        .map(member -> validateType(email, role, member, "개인 회원이"))
                        .orElseThrow(() -> {
                            log.warn("개인회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 개인 회원이 존재하지 않습니다.");
                        });

            case COMPANY:
                return companyRepository.findByEmail(email)
                        .map(company -> validateType(email, role, company, "기업 회원이"))
                        .orElseThrow(() -> {
                            log.warn("기업회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 기업 회원이 존재하지 않습니다.");
                        });

            case ADMIN:
                return memberRepository.findByEmail(email)
                        .map(member -> validateType(email, role, member, "관리자가"))
                        .orElseThrow(() -> {
                            log.warn("관리자 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 관리자가 존재하지 않습니다.");
                        });

            default:
                log.error("❌ 알 수 없는 role: {}", role);
                throw new IllegalArgumentException("지원하지 않는 role: " + role);
        }
    }

    private MBotUserDetails validateType(String email, Role role, Member member, String label) {
        if (member.getRole() != role) {
            log.warn("{} 타입 불일치 -  email: {}, expectedRole: {}, frondInput: {}", label, member.getEmail(), member.getRole(), role);
            throw new InvalidRoleException(label + " 아닙니다.");
        }
        log.warn("{} 로그인 성공 - {}", label, email);

        return new MBotUserDetails(
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole(),
                member.getId(),
                member.getStatus()
        );
    }

    private MBotUserDetails validateType(String email, Role role, Company company, String label) {
        if (company.getRole() != role) {
            log.warn("{} 타입 불일치 -  email: {}, expectedRole: {}, frondInput: {}", label, company.getEmail(), company.getRole(), role);
            throw new InvalidRoleException(label + " 아닙니다.");
        }
        log.warn("{} 로그인 성공 - {}", label, email);

        return new MBotUserDetails(
                company.getName(),
                company.getEmail(),
                company.getPassword(),
                company.getRole(),
                company.getId(),
                company.getStatus()
        );
    }

}
