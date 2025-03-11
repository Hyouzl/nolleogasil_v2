package com.fourroro.nolleogasil_backend.entity.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class PrincipalDetails implements UserDetails, OAuth2User {

    private final Users user;
    private final Map<String, Object> attributes;
    private final String subject;

    // 일반 로그인 생성자
    public PrincipalDetails(Users user) {
        this.user = user;
        this.attributes = null;
        this.subject = null;
    }

    public PrincipalDetails(Users user, Map<String, Object> attributes, String subject) {
        this.user = user;
        this.attributes = attributes;
        this.subject = subject;
    }

    // OAuth2 로그인 생성자

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        if (subject != null) {
            return subject;
        }
        return user.getEmail();
    }

    public String getSubject() {
        return subject;
    }
    public Long getUserId() {return this.user.getUsersId();}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료되지 않음
    }
    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠기지 않음
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
