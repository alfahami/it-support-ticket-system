package com.codelogium.ticketing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.dto.TicketInfoUpdateDTO;
import com.codelogium.ticketing.dto.TicketStatusUpdateDTO;
import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.entity.enums.Status;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
import com.codelogium.ticketing.repository.AuditLogRepository;
import com.codelogium.ticketing.repository.TicketRepository;
import com.codelogium.ticketing.repository.UserRepository;

import jakarta.transaction.Transactional;

import static com.codelogium.ticketing.util.EntityUtils.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImp implements TicketService {

    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private AuditLogRepository auditLogRepository;

    @Override
    public Ticket createTicket(Long userId, Ticket newTicket) {
        User user = UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        newTicket.setCreator(user);
        newTicket.setStatus(Status.NEW); // default status
        newTicket.setCreationDate(Instant.now());

        Ticket createdTicket = ticketRepository.save(newTicket);

        // Log ticket creation
        auditLogRepository.save(new AuditLog(
                null,
                createdTicket.getId(),
                null,
                userId,
                "TICKET_CREATED",
                null,
                createdTicket.getStatus().toString(),
                Instant.now()));

        auditLogRepository.flush(); // Ensure immediate persistence

        return createdTicket;
    }

    // Update Ticket by the creator
    @Transactional
    @Override
    public Ticket updateTicketInfo(Long ticketId, Long userId, TicketInfoUpdateDTO dto) {
        // Verify user existS
        validateUser(userId);

        // Get the ticket and verify it belongs to this user
        Ticket retrievedTicket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        updateIfNotNull(retrievedTicket::setTitle, dto.getTitle());
        updateIfNotNull(retrievedTicket::setDescription, dto.getDescription());
        updateIfNotNull(retrievedTicket::setCategory, dto.getCategory());
        updateIfNotNull(retrievedTicket::setPriority, dto.getPriority());

        // Save ticket update
        return ticketRepository.save(retrievedTicket);

    }

    @Transactional
    @Override
    public Ticket updateTicketStatus(Long ticketId, Long userId, TicketStatusUpdateDTO dto) {
        // validate user exists
        validateUser(userId);

        // Get the ticket and verify it belongs to this user
        Ticket retrievedTicket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        // Store old status before making changes
        Status oldStatus = retrievedTicket.getStatus();

        updateIfNotNull(retrievedTicket::setStatus, dto.getStatus());

        Ticket savedTicket = ticketRepository.save(retrievedTicket);
        // Log status change if only there's an actual modification
        if (isStatusChanged(oldStatus, dto.getStatus())) {
            auditLogRepository.save(new AuditLog(
                    null,
                    ticketId,
                    null,
                    userId,
                    "STATUS_UPDATED",
                    oldStatus.toString(),
                    dto.getStatus().toString(),
                    Instant.now()));

            auditLogRepository.flush(); // Ensure immediate persistence

        }
        return savedTicket;
    }

    @Override
    public Ticket retrieveTicket(Long ticketId, Long userId) {

        // validate user exists
        validateUser(userId);
        
        // ensure relationship user->ticket and retrieved ticket
        return unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));
    }

    @Override
    public List<Ticket> retrieveTicketsByCreator(Long userId) {
        validateUser(userId);

        List<Ticket> tickets = ticketRepository.findByCreatorId(userId);
        if( tickets == null || tickets.size() == 0) throw new ResourceNotFoundException("No tickets created yet.");

        return tickets;
    }

    @Override
    public Ticket searchTicket(Long ticketId, Long userId, Status status) {
        validateUser(userId);
        
        Ticket ticket = unwrapTicket(ticketId, ticketRepository.findByTicketIdAndStatus(ticketId, status));   
        
        return ticket;
    }

    @Override
    public List<AuditLog> retrieveAuditLogs(Long ticketId, Long userId) {
        // Verify user existence by checking the creator relationship
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        return auditLogRepository.findByTicketId(ticketId);
    }

    @Override
    public void removeTicket(Long ticketId, Long userId) {
        // validate user exists
        validateUser(userId);

        // Get the ticket
        Ticket ticket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        User user = ticket.getCreator();

        user.getTickets().remove(ticket); // remove ticket from the user's list before updating

        userRepository.save(user); // Save user to updated reference, this will trigger orphanRemoval thus no need to manually delete the ticket from tickerRepository 
    }

    // We're just validating, not actually querying and extracting the db
    /*
     * Possible to dedicate an entity validation service, that would centralize validation but nothing complex here, thus the duplication method in ticket service and comment service
     */
    private void validateUser(Long userId) {
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException(userId, User.class);
    }

    private boolean isStatusChanged(Status oldStatus, Status newStatus) {
        return newStatus != null && !oldStatus.equals(newStatus);
    }

    public static Ticket unwrapTicket(Long ticketId, Optional<Ticket> optionalTicket) {
        return optionalTicket.orElseThrow(() -> new ResourceNotFoundException(ticketId, Ticket.class));
    }
}
