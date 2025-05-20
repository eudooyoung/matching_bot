package com.multi.matchingbot.common.security;

import com.multi.matchingbot.company.CompanyRepository;
import com.multi.matchingbot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingBotUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new MatchingBotUserDetails(user.getEmail(), user.getPassword(), user.getRole()))
                .orElseGet(() -> companyRepository.findByEmail(email)
                        .map(company -> new MatchingBotUserDetails(company.getEmail(), company.getPassword(), company.getRole()))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
                );

    }
}
