package com.codelogium.ticketing.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.codelogium.ticketing.entity.enums.Category;
import com.codelogium.ticketing.entity.enums.Priority;
import com.codelogium.ticketing.entity.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank or null")
    private String title;

    @NotBlank(message = "Desscription cannot be blank or null")
    private String description;

    @NotNull(message = "Creation date is required")
    private LocalDateTime creationDate;

    private User creator;

    private Status status;

    private Category category;
    
    private Priority priority;

    private List<Comment> comments = new ArrayList<>();

}
