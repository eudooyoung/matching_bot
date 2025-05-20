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

//        System.out.println("✅ DB password: [" + company.getPassword() + "]");

        /*log.warn("loadUserByUsername 확인");
        return userRepository.findByEmail(email)
                .map(user -> new MBotUserDetails(user.getEmail(), user.getPassword(), user.getRole(), "USER", user.getId()))
                .orElseGet(() -> companyRepository.findByEmail(email)
                        .map(company -> new MBotUserDetails(company.getEmail(), company.getPassword(), company.getRole(), "COMPANY", company.getId()))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
                );*/

        log.warn("loadUserByUsername 확인");

        return userRepository.findByEmail(email)
                .map(user -> {
                    log.warn("✅ DB password (user): [{}]", user.getPassword());
                    return new MBotUserDetails(
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole(),
                            "USER",
                            user.getId()
                    );
                })
                .orElseGet(() -> companyRepository.findByEmail(email)
                        .map(company -> {
                            log.warn("✅ DB password (company): [{}]", company.getPassword());
                            return new MBotUserDetails(
                                    company.getEmail(),
                                    company.getPassword(),
                                    company.getRole(),
                                    "COMPANY",
                                    company.getId()
                            );
                        })
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
                );

    }
}
