package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.exception.ErrorResponse;
import com.codelogium.ticketing.service.TicketService;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Ticket Controller", description = "Manages support tickets for users")
@RequestMapping(value = "/users/{userId}/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private final TicketService ticketService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket successfully created", content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: unsuccessful submission")
    })
    @Operation(summary = "Create Ticket", description = "Creates a new support ticket")
    @PostMapping
    public ResponseEntity<String> createTicket(@PathVariable Long userId, @RequestBody @Valid Ticket newTicket) {
        ticketService.createTicket(userId, newTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket created successfully");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket successfully retrieved", content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Get Ticket", description = "Retrieves a ticket by ID")
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable Long userId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.retrieveTicket(ticketId, userId));
    }

    //TODO: Implement Update Ticket Handler

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs successfully retrieved", content = @Content(schema = @Schema(implementation = AuditLog.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Get Audit Logs", description = "Retrieves audit logs of a ticket")
    @GetMapping("/{ticketId}/audit-logs")
    public ResponseEntity<List<AuditLog>> retrieveAuditLogs(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.retrieveAuditLogs(ticketId, ticketId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "Delete Ticket", description = "Deletes a ticket by ID")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<HttpStatus> removeTicket(@PathVariable Long userId, @PathVariable Long ticketId) {
        // TODO: fix the swapped uri path variable
        ticketService.removeTicket(userId, ticketId);
        return ResponseEntity.noContent().build();
    }
}
