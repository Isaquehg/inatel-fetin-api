package com.innov8ors.backend.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails{

    private User user;

    public UserDetailsImpl(User user){
        this.user = user;
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (user.getRole()) {
            case "ADVISOR":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADVISOR"));
            case "MEMBER":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER"));
            case "ADMIN":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default:
                // Role doesn't exist
                return Collections.emptyList();
        }
    }
    
}
