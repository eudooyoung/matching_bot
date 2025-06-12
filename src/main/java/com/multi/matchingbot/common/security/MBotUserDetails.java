package com.multi.matchingbot.common.security;


import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
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

    private final String name;
    private final String email;
    private final String password;
    private final Role role;
    private final Long id;
    private final Yn status;

    public MBotUserDetails(String name, String email, String password, Role role, Long id, Yn status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.id = id;
        this.status = status;
        log.warn("MBotUserDetails 생성 완료 - email: {}, role: {},  userId: {}, status: {}", email, role, id, status);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        log.warn("권한 반환 - ROLE_{}", role);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
//        log.warn("getPassword() 호출됨: {}", password);
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
        return Yn.Y.equals(this.status);
    }

    public Long getCompanyId() {
        return this.id;
    }

    public Long getMemberId() {
        return this.id;
    }

    public String getCompanyName() {
        return this.name;
    }
}
