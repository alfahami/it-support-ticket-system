package com.codelogium.ticketing.service;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.repository.TicketRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImp implements TicketService {
    
    private TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(Ticket newTicket) {
        return ticketRepository.save(newTicket);
    }
}
