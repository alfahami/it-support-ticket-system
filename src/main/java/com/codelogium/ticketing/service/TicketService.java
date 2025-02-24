package com.codelogium.ticketing.service;

import java.util.List;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Long userId, Ticket newTicket);
    Ticket updateTicket(Long userId, Long ticketId, Ticket newTicket);
    void removeTicket(Long userid, Long ticketId);
    List<Ticket> retrieveTicketsByCreator(Long userId);
    
}
