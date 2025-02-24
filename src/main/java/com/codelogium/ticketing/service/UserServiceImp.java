package com.codelogium.ticketing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
import com.codelogium.ticketing.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(Long userId) {
        return unwrapUser(userId, userRepository.findById(userId));
    }

    public void validateUserExists(Long userId) {
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException(userId, User.class);
    }

    public static User unwrapUser(Long userId, Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
    }
}
