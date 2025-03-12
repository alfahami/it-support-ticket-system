package com.codelogium.ticketing.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.codelogium.ticketing.security.rbac.CustomUserDetailsServiceImp;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    
    private CustomUserDetailsServiceImp customUserDetailsServiceImp;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDetails userDetails  = customUserDetailsServiceImp.loadUserByUsername(authentication.getName());

        if(!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), userDetails.getPassword(), userDetails.getAuthorities());
    }
}
