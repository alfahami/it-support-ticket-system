package com.codelogium.ticketing.service;

import static com.codelogium.ticketing.util.EntityUtils.*;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.Comment;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
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

    @Override
    public Comment updateComment(Long commentId, Long ticketId, Long userId, Comment newComment) {
        UserServiceImp.unwrapUser(userId, ticketRepository.findCreatorByTicket(ticketId));

        TicketServiceImp.unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        Comment retrievedComment = unwrapComment(commentId, commentRepository.findByIdAndTicketIdAndCreatorId(commentId, ticketId, userId));

        newComment.setId(retrievedComment.getId()); // prevent intentional ID tampering
        //TODO: timestamp should be automatically changed when the user has made some changes
        updateIfNotNull(retrievedComment::setAuthor, newComment.getAuthor());
        updateIfNotNull(retrievedComment::setContent, newComment.getContent());
        updateIfNotNull(retrievedComment::setTicket
        , newComment.getTicket());
        updateIfNotNull(retrievedComment::setTimestamp, newComment.getTimestamp());

        return commentRepository.save(retrievedComment);
    }

    public static Comment unwrapComment(Long commentId, Optional<Comment> optionalComment) {
        return optionalComment.orElseThrow(() -> new ResourceNotFoundException(commentId, Comment.class));
    }
}
