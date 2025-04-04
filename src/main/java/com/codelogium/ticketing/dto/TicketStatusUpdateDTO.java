package com.codelogium.ticketing.dto;

import com.codelogium.ticketing.entity.enums.Status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketStatusUpdateDTO {
    
    @NotNull(message = "Status cannot be null")
    private Status status;
}
