package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    
    private UserService userService;

    // We could use UserDTO and AuthController in order to filter and not send username, passoword and other sensitive data but let's just send 201 Http status creation
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> retrieveUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.retrieveUser(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }
}
