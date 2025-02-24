package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.ticketing.entity.Ticket;
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



}
