package com.multi.matchingbot.common.security;

import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.user.UserRepository;
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
        throw new UnsupportedOperationException("이 서비스는 loadByType(...)만 지원합니다.");
    }

    public UserDetails loadByType(String email, String userType) {
        log.warn("▶ loadByType 호출 - email: {}, userType: {}", email, userType);

        switch (userType.toUpperCase()) {
            case "USER":
                return userRepository.findByEmail(email)
                        .map(user -> {
                            log.warn("✔ 개인회원 로그인 성공 - {}", email);
                            return new MBotUserDetails(
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getRole(),
//                                    "USER",
                                    user.getId()
                            );
                        })
                        .orElseThrow(() -> {
                            log.warn("✖ 개인회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("개인회원 없음: " + email);
                        });

            case "COMPANY":
                return companyRepository.findByEmail(email)
                        .map(company -> {
                            log.warn("✔ 기업회원 로그인 성공 - {}", email);
                            return new MBotUserDetails(
                                    company.getEmail(),
                                    company.getPassword(),
                                    company.getRole(),
//                                    "COMPANY",
                                    company.getId()
                            );
                        })
                        .orElseThrow(() -> {
                            log.warn("✖ 기업회원 조회 실패 - {}", email);
                            return new UsernameNotFoundException("기업회원 없음: " + email);
                        });

            case "ADMIN":
                return userRepository.findByEmail(email)
                        .map(user -> {
                            log.warn("✔ 관리자 로그인 성공 - {}", email);
                            return new MBotUserDetails(
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getRole(),
//                                    "ADMIN",
                                    user.getId()
                            );
                        })
                        .orElseThrow(() -> {
                            log.warn("✖ 관리자 조회 실패 - {}", email);
                            return new UsernameNotFoundException("관리자 없음: " + email);
                        });

            default:
                log.error("❌ 알 수 없는 userType: {}", userType);
                throw new IllegalArgumentException("지원하지 않는 userType: " + userType);
        }
    }
}
