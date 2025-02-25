package com.codelogium.ticketing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.codelogium.ticketing.security.filter.AuthenticationFilter;
import com.codelogium.ticketing.security.filter.ExceptionHandlerFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        // setting the uri for our authentication
        authenticationFilter.setFilterProcessesUrl("/user/authenticate");
        http
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers("/h2/**").permitAll()
            .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class) // first filter to run before any filter
            .addFilter(authenticationFilter)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            return http.build();
    }
}
