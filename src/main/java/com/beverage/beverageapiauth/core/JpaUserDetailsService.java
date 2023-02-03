package com.beverage.beverageapiauth.core;

import com.beverage.beverageapiauth.domain.RepositoryUser;
import com.beverage.beverageapiauth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repositoryUser.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the email informed"));

        return new AuthUser(user);
    }
}
