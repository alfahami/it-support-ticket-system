package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import com.codelogium.ticketing.dto.TicketInfoUpdateDTO;
import com.codelogium.ticketing.dto.TicketStatusUpdateDTO;
import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.enums.Status;
import com.codelogium.ticketing.exception.ErrorResponse;
import com.codelogium.ticketing.service.TicketService;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@EnableMethodSecurity // apply security measures at method level
@AllArgsConstructor
@Tag(name = "Ticket Controller", description = "Manages support tickets for users")
@RequestMapping(value = "/users/{userId}/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private final TicketService ticketService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket successfully created", content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: unsuccessful submission"),
            @ApiResponse(ref = "#/components/responses/401"),
            @ApiResponse(ref = "#/components/responses/403")
    })
    @Operation(summary = "Create Ticket", description = "Creates a new support ticket")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<String> createTicket(@PathVariable Long userId, @RequestBody @Valid Ticket newTicket) {
        ticketService.createTicket(userId, newTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket created successfully");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket successfully retrieved", content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(ref = "#/components/responses/401")
    })
    @Operation(summary = "Get Ticket", description = "Retrieves a ticket by ID")
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable Long userId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.retrieveTicket(ticketId, userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket successfully updated", content = @Content(schema = @Schema(implementation = Ticket.class))),
        @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(ref = "#/components/responses/401"),
        @ApiResponse(ref = "#/components/responses/403")
    })
    @Operation(summary = "Update Ticket Info",  description="Update an existing ticket's details")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PatchMapping("/{ticketId}/info")
    public ResponseEntity<Ticket> updateTicketInfo(@PathVariable Long ticketId, @PathVariable Long userId, @RequestBody @Valid TicketInfoUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicketInfo(ticketId, userId, dto));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket successfully updated", content = @Content(schema = @Schema(implementation = Ticket.class))),
        @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(ref = "#/components/responses/401"),
        @ApiResponse(ref = "#/components/responses/403")
    })
    @Operation(summary = "Update Ticket Status",  description="Update an existing ticket's status")
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long ticketId, @PathVariable Long userId, @RequestBody @Valid TicketStatusUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(ticketId, userId, dto));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tickets successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
        @ApiResponse(responseCode = "404", description = "Tickets not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(ref = "#/components/responses/401")
    })
    @Operation(summary = "Get All Tickets", description = "Retrieves all user's existing tickets")
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> retrieveTicketsByCreator(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.retrieveTicketsByCreator(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket successfully found", content = @Content(schema = @Schema(implementation = Ticket.class))),
        @ApiResponse(responseCode = "404", description = "Ticket or user not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(ref = "#/components/responses/401")
    })
    @Operation(summary = "Search Ticket", description = "Search ticket by its id and status")
    @GetMapping("{ticketId}/search")
    public ResponseEntity<Ticket> searchByIdAndStatus(@PathVariable Long ticketId, @PathVariable Long userId, @RequestParam Status status) {
        return ResponseEntity.ok(ticketService.searchTicket(ticketId, userId, status));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs successfully retrieved", content = @Content(schema = @Schema(implementation = AuditLog.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(ref = "#/components/responses/401"),
            @ApiResponse(ref = "#/components/responses/403")
    })
    @Operation(summary = "Audit Tickets Logs", description = "Retrieves audit logs of a ticket")
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @GetMapping("/{ticketId}/audit-logs")
    public ResponseEntity<List<AuditLog>> retrieveAuditLogs(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.retrieveAuditLogs(ticketId, ticketId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(ref = "#/components/responses/401")
    })
    @Operation(summary = "Delete Ticket", description = "Deletes a ticket by ID")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> removeTicket(@PathVariable Long userId, @PathVariable Long ticketId) {
        ticketService.removeTicket(ticketId, userId);
        return ResponseEntity.noContent().build();
    }
}
