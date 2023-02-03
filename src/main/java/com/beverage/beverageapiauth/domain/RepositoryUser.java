package com.beverage.beverageapiauth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryUser extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
