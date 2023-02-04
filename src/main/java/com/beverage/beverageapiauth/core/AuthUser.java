package com.beverage.beverageapiauth.core;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class AuthUser extends User {

    private static final long serialVersionUID = 1L;

    private String fullName;
    private Long userId;

    public AuthUser(com.beverage.beverageapiauth.domain.User user) {
        super(user.getEmail(), user.getPassword(), Collections.emptyList());

        this.fullName = user.getName();
        this.userId = user.getId();
    }
}
