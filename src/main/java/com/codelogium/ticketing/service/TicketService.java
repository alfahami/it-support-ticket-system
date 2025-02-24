package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Long userId, Ticket newTicket);
    Ticket updateTicket(Long userId, Long ticketId, Ticket newTicket);
    void removeTicket(Long userid, Long ticketId);
    
}
