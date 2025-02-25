package com.codelogium.ticketing.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codelogium.ticketing.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
                
                // Deserializing the incoming data from the request to a User object
                try {
                    User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Password: " + user.getPassword());
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                
        return super.attemptAuthentication(request, response);
    }
}
