package com.beverage.beverageapiauth.core;

import com.beverage.beverageapiauth.domain.RepositoryUser;
import com.beverage.beverageapiauth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repositoryUser.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the email informed"));

        return new AuthUser(user, getAuthorities(user));
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        return user.getGroups().stream()
                .flatMap(group -> group.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
