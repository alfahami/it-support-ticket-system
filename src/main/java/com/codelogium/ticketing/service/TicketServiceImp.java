package com.codelogium.ticketing.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
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
    public Ticket updateTicket(Long userId, Long ticketId, Ticket newTicket) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        Ticket retrievedTicket = unwrapTicket(ticketId, ticketRepository.findByIdAndUserId(ticketId, userId));

        newTicket.setId(retrievedTicket.getId()); // ignoring ID in request body as it might be tampered
        updateIfNotNull(retrievedTicket::setTitle, newTicket.getTitle());
        updateIfNotNull(retrievedTicket::setDescription, newTicket.getDescription());
        updateIfNotNull(retrievedTicket::setCategory, newTicket.getCategory());
        updateIfNotNull(retrievedTicket::setPriority, newTicket.getPriority());
        updateIfNotNull(retrievedTicket::setStatus, newTicket.getStatus());
        
        return ticketRepository.save(retrievedTicket);
    }

    public static Ticket unwrapTicket(Long ticketId, Optional<Ticket> optionalTicket) {
        return optionalTicket.orElseThrow(() -> new ResourceNotFoundException(ticketId, Ticket.class));
    }
}
