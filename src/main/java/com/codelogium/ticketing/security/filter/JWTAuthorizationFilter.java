package com.codelogium.ticketing.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codelogium.ticketing.security.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    // Authorization: Bearer JWT 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization"); //Bearer JWT
        
        // Checks if user requested register uri -no bearer-
        if(header == null || !header.startsWith(SecurityConstants.BEARER)) {
            filterChain.doFilter(request, response);
            return; // No need to keep going after the registration uri is performed
        }

        String token = header.replace(SecurityConstants.BEARER, "");
        // if no exception is raised, the username is extracted as it is the subject we passed to the token
        String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
        .build().verify(token)
        .getSubject();
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(token, user, Arrays.asList());
        // Setting the authentication to the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Allowing the authorized user to perform it request
        filterChain.doFilter(request, response);

    }
    
}
