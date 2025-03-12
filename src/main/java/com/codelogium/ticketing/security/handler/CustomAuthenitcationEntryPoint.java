package com.codelogium.ticketing.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.codelogium.ticketing.security.filter.ExceptionHandlerFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenitcationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        ExceptionHandlerFilter.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "ACESS DENIED: AUTHENITCATION REQUIRED");
    }
    
}
