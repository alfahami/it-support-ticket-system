package com.codelogium.ticketing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        newTicket.setTimestamp(Instant.now());
        return ticketRepository.save(newTicket);
    }

    @Transactional
    @Override
    public Ticket updateTicket(Long ticketId, Long userId, Ticket newTicket) {
        // Verify user existance by checking the creator relationship
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        // Get the ticket and verify it belongs to this user
        Ticket retrievedTicket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        // Store old status before making changes
        Status oldStatus = retrievedTicket.getStatus();

        updateIfNotNull(retrievedTicket::setTitle, newTicket.getTitle());
        updateIfNotNull(retrievedTicket::setDescription, newTicket.getDescription());
        updateIfNotNull(retrievedTicket::setCategory, newTicket.getCategory());
        updateIfNotNull(retrievedTicket::setPriority, newTicket.getPriority());
        updateIfNotNull(retrievedTicket::setStatus, newTicket.getStatus());

        // Save ticket update
        ticketRepository.save(retrievedTicket);

        // Log status change if only there's an actual modification
        if (isStatusChanged(oldStatus, newTicket.getStatus())) {
            auditLogRepository.save(new AuditLog(null, ticketId, userId, "STATUS_UPDATED", oldStatus.toString(),
                    newTicket.getStatus().toString(), Instant.now()));
            auditLogRepository.flush(); // Ensure immediate persistence

        }

        return retrievedTicket;

    }

    @Override
    public Ticket retrieveTicket(Long ticketId, Long userId) {
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        return unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));
    }

    @Override
    public List<Ticket> retrieveTicketsByCreator(Long userId) {
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(userId));
        return ticketRepository.findByCreatorId(userId);
    }

    @Override
    public List<Ticket> searchTickets(Long ticketId, Status status) {
        return ticketRepository.findByTicketIdAndStatus(ticketId, status);
    }

    @Override
    public void removeTicket(Long ticketId, Long userId) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        Ticket ticket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        ticketRepository.delete(ticket);
    }

    private boolean isStatusChanged(Status oldStatus, Status newStatus) {
        return newStatus != null && !oldStatus.equals(newStatus);
    }

    public static Ticket unwrapTicket(Long ticketId, Optional<Ticket> optionalTicket) {
        return optionalTicket.orElseThrow(() -> new ResourceNotFoundException(ticketId, Ticket.class));
    }
}
