package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.User;

public interface UserService {
    User createUser(User user);
    User retrieveUser(Long userId);
    User retrieveUser(String username);
    void removeUser(Long userId);
} 
