package com.codelogium.ticketing.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.codelogium.ticketing.security.filter.ExceptionHandlerFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ExceptionHandlerFilter.sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "ACCESS DENIED: YOU DON'T HAVE PERMISSION");
    }
    
}
