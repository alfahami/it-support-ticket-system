package com.codelogium.ticketing.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.enums.Status;
import com.codelogium.ticketing.service.TicketService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "users/{userId}/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@PathVariable Long userId, @RequestBody @Valid Ticket newTicket) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(userId, newTicket));
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @PathVariable Long userId, @RequestBody @Valid Ticket newTicket) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, userId, newTicket));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable Long ticketId, @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.retrieveTicket(ticketId, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> retrieveAllTicketsByCreator(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.retrieveTicketsByCreator(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(@RequestParam(required = false) Long ticketId, @RequestParam(required = false) Status status) {
        return ResponseEntity.ok(ticketService.searchTickets(ticketId, status));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<HttpStatus> removeTicket(@PathVariable Long userId, @PathVariable Long ticketId) {
        ticketService.removeTicket(userId, ticketId);
        return ResponseEntity.noContent().build();
    }
}
