package com.codelogium.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
