package com.codelogium.ticketing.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(Long id, Class<?> entityClass) {
        super("The " + entityClass.getSimpleName().toLowerCase() + " with the id " + id + " is not found");
    }
}
