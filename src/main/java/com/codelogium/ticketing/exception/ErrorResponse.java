package com.codelogium.ticketing.exception;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant timestamp;
    private List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.timestamp = Instant.now();
        this.messages = messages;
    }
}
