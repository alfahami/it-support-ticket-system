package com.codelogium.ticketing.service;

import java.util.List;

import com.codelogium.ticketing.dto.TicketInfoUpdateDTO;
import com.codelogium.ticketing.dto.TicketStatusUpdateDTO;
import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.enums.Status;

public interface TicketService {
    Ticket createTicket(Long userId, Ticket newTicket);
    Ticket updateTicketInfo(Long ticketId, Long userId, TicketInfoUpdateDTO dto);
    Ticket updateTicketStatus(Long ticketId, Long userId, TicketStatusUpdateDTO dto);
    Ticket retrieveTicket(Long ticketId, Long userId);
    void removeTicket(Long ticketId, Long userid);
    List<Ticket> retrieveTicketsByCreator(Long userId);

    Ticket searchTicket(Long ticketId, Long userId, Status status);
    List<AuditLog> retrieveAuditLogs(Long ticketId, Long userId);
    
}
