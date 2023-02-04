package com.beverage.beverageapiauth.core;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class AuthUser extends User {

    private static final long serialVersionUID = 1L;

    private String fullName;
    private Long userId;

    public AuthUser(com.beverage.beverageapiauth.domain.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);

        this.fullName = user.getName();
        this.userId = user.getId();
    }
}
