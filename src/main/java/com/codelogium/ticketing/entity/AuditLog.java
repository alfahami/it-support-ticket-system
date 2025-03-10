package com.codelogium.ticketing.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;   // Tracks which tickets was changed
    private Long commentId;
    private Long userId;     // Who made the change
    private String action;   // What was made: STATUS_UPDATED, COMMENT_ADDED
    private String oldValue; // Previous status (or null if creation)
    private String newValue; // New status or comment

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant timestamp = Instant.now(); // when the change happened

}
