package com.codelogium.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
