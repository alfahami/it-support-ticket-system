package com.codelogium.ticketing.service;

import java.util.List;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Long userId, Ticket newTicket);
    Ticket updateTicket(Long ticketId, Long userId, Ticket newTicket);
    Ticket retrieveTicket(Long ticketId, Long userId);
    void removeTicket(Long ticketId, Long userid);
    List<Ticket> retrieveTicketsByCreator(Long userId);
    
}
