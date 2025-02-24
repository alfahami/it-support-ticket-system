package com.codelogium.ticketing.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Comment;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.repository.CommentRepository;
import com.codelogium.ticketing.repository.TicketRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImp implements CommentService {

    private CommentRepository commentRepository;
    private TicketRepository ticketRepository;

    @Override
    public Comment createComment(Long ticketId, Long userId, Comment newComment) {
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        Ticket retrieveTicket = TicketServiceImp.unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));
        newComment.setTicket(retrieveTicket);

        newComment.setTimestamp(Instant.now());
        return commentRepository.save(newComment);
    }
}
