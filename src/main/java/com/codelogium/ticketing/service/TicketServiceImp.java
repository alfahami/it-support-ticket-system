package com.codelogium.ticketing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.entity.enums.Status;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
import com.codelogium.ticketing.repository.TicketRepository;
import com.codelogium.ticketing.repository.UserRepository;
import static com.codelogium.ticketing.util.EntityUtils.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImp implements TicketService {

    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    @Override
    public Ticket createTicket(Long userId, Ticket newTicket) {
        User user = UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        newTicket.setCreator(user);
        newTicket.setTimestamp(Instant.now());
        return ticketRepository.save(newTicket);
    }

    @Override
    public Ticket updateTicket(Long ticketId, Long userId, Ticket newTicket) {
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        Ticket retrievedTicket = unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        newTicket.setId(retrievedTicket.getId()); // ignoring ID in request body as it might be tampered
        updateIfNotNull(retrievedTicket::setTitle, newTicket.getTitle());
        updateIfNotNull(retrievedTicket::setDescription, newTicket.getDescription());
        updateIfNotNull(retrievedTicket::setCategory, newTicket.getCategory());
        updateIfNotNull(retrievedTicket::setPriority, newTicket.getPriority());
        updateIfNotNull(retrievedTicket::setStatus, newTicket.getStatus());
        
        return ticketRepository.save(retrievedTicket);
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

    public static Ticket unwrapTicket(Long ticketId, Optional<Ticket> optionalTicket) {
        return optionalTicket.orElseThrow(() -> new ResourceNotFoundException(ticketId, Ticket.class));
    }
}
