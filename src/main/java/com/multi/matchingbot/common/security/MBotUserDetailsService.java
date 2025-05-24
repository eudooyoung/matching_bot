package com.multi.matchingbot.common.security;

import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.error.InvalidRoleException;
import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.user.UserRepository;
import com.multi.matchingbot.user.domain.User;
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

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("이 서비스는 loadUserByType(...)만 지원합니다.");
    }

    public UserDetails loadUserByType(String email, Role role) {
        log.warn("▶ loadByType 호출 - email: {}, role: {}", email, role);

        switch (role) {
            case USER:
                return userRepository.findByEmail(email)
                        .map(user -> validateType(email, role, user, "개인 회원"))
                        .orElseThrow(() -> {
                            log.warn("개인회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 개인 회원이 존재하지 않습니다.");
                        });

            case COMPANY:
                return companyRepository.findByEmail(email)
                        .map(company -> validateType(email, role, company, "기업 회원"))
                        .orElseThrow(() -> {
                            log.warn("기업회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 기업 회원이 존재하지 않습니다.");
                        });

            case ADMIN:
                return userRepository.findByEmail(email)
                        .map(user -> validateType(email, role, user, "관리자"))
                        .orElseThrow(() -> {
                            log.warn("관리자 조회 실패 - {}", email);
                            return new UsernameNotFoundException("해당 이메일의 관리자가 존재하지 않습니다.");
                        });

            default:
                log.error("❌ 알 수 없는 role: {}", role);
                throw new IllegalArgumentException("지원하지 않는 role: " + role);
        }
    }

    private MBotUserDetails validateType(String email, Role role, User user, String label) {
        if (user.getRole() != role) {
            log.warn("{} 타입 불일치 -  email: {}, expectedRole: {}, frondInput: {}", label, user.getEmail(), user.getRole(), role);
            throw new InvalidRoleException(label + "이 아닙니다.");
        }
        log.warn("{} 로그인 성공 - {}", label, email);

        return new MBotUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getId()
        );
    }

    private MBotUserDetails validateType(String email, Role role, Company company, String label) {
        if (company.getRole() != role) {
            log.warn("{} 타입 불일치 -  email: {}, expectedRole: {}, frondInput: {}", label, company.getEmail(), company.getRole(), role);
            throw new InvalidRoleException(label + "이 아닙니다.");
        }
        log.warn("{} 로그인 성공 - {}", label, email);

        return new MBotUserDetails(
                company.getEmail(),
                company.getPassword(),
                company.getRole(),
                company.getId()
        );
    }

}
