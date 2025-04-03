package com.codelogium.ticketing.security.rbac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.entity.enums.UserRole;

import lombok.AllArgsConstructor;

/*
 * - This class is used to represent the details of a user during the authentication and authorization process.
 * - Acts as a bridge between your application's user model and Spring Security's authentication mechanisms.
 * - It uses this object to store and retrieve user information during the authentication process.
 * - It holds the essential details that Spring Security needs, like the username, password, and authorities (roles/permissions).
 * - This allows Spring to know which roles the user has and enforce authorization rules accordingly.
 * - This is a simplified representation of the user entity that focuses on authentication and authorization.
 */

@AllArgsConstructor
public class CustomUserDetailsImp implements UserDetails {

    private User user;

    /*
     * Even if user has single role, we must return a collection of authority.
     * This because Spring Security is designed to support multiple authorities per user, as roles are often cumulative or combined.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        UserRole role = user.getRole();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    /*
     * Strictly related to the user account state, to not confuse with JWT expiration
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
