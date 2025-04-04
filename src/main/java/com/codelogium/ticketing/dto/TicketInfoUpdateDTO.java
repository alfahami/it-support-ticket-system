package com.codelogium.ticketing.dto;

import java.time.Instant;

import com.codelogium.ticketing.entity.enums.Category;
import com.codelogium.ticketing.entity.enums.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketInfoUpdateDTO {
    
    @NotBlank(message = "Title cannot be blank or null")
    private String title;

    @NotBlank(message = "Description cannot be blank or null")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant creationDate;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    private Category category;
    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;
}
