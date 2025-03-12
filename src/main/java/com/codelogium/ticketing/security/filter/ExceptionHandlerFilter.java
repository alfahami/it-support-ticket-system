package com.codelogium.ticketing.security.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.codelogium.ticketing.exception.ResourceNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ResourceNotFoundException e) {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Username doesn't exist");
        } catch (JWTVerificationException e) {
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "JWT NOT VALID");
        } catch (RuntimeException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "BAD REQUEST");
        }
    }

    public static void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
