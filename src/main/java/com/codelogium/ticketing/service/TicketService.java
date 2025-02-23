package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket newTicket);
    
}
