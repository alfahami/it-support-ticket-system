package com.codelogium.ticketing.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
import com.codelogium.ticketing.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User createUser(User user) {
        // Encoding the password before saving
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(Long userId) {
        return unwrapUser(userId, userRepository.findById(userId));
    }

    // Retrieves a user based on username
    @Override
    public User retrieveUser(String username) {
        return unwrapUser(404L, userRepository.findByUsername(username));
    }

    @Override
    public void removeUser(Long userId) {
        User retrievedUser = unwrapUser(userId, userRepository.findById(userId));

        userRepository.delete(retrievedUser);
    }

    public void validateUserExists(Long userId) {
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException(userId, User.class);
    }

    public static User unwrapUser(Long userId, Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
    }
}
