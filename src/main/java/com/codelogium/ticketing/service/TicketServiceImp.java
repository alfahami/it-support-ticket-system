package com.codelogium.ticketing.service;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.repository.TicketRepository;
import com.codelogium.ticketing.repository.UserRepository;

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
        return ticketRepository.save(newTicket);
    }
}
