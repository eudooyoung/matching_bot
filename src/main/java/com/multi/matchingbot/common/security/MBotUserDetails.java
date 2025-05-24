package com.multi.matchingbot.common.security;

import com.multi.matchingbot.common.domain.enums.Role;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
//@AllArgsConstructor
public class MBotUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final Role role;
//    private final String userType;
    private final Long id;

    public MBotUserDetails(String email, String password, Role role, /*String userType,*/ Long id) {
        this.email = email;
        this.password = password;
        this.role = role;
//        this.userType = userType;
        this.id = id;
        log.warn("MBotUserDetails 생성 완료 - email: {}, role: {},  id: {}", email, role, /*userType,*/ id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.warn("권한 반환 - ROLE_{}", role);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        log.warn("getPassword() 호출됨: {}", password);
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
