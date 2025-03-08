package com.codelogium.ticketing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.codelogium.ticketing.security.filter.AuthenticationFilter;
import com.codelogium.ticketing.security.filter.ExceptionHandlerFilter;
import com.codelogium.ticketing.security.filter.JWTAuthorizationFilter;
import com.codelogium.ticketing.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    CustomAuthenticationManager CustomAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(CustomAuthenticationManager);
        // setting the uri for our authentication
        authenticationFilter.setFilterProcessesUrl("/user/authenticate");
        http
            .headers(headers -> headers.disable())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize  
            .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
            .requestMatchers("/swagger-ui/*", "/api-docs/**", "/h2-console/*").permitAll() // allows swagger ui to public user
            .anyRequest().authenticated())
            .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class) // first filter to run before any filter
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
    }
}
