package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.User;

public interface UserService {
    User createUser(User user);
    User retrieveUser(Long userId);
    void removeUser(Long userId);
} 
