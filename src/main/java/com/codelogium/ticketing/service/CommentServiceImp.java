package com.codelogium.ticketing.service;

import static com.codelogium.ticketing.util.EntityUtils.*;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Comment;
import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.exception.ResourceNotFoundException;
import com.codelogium.ticketing.repository.AuditLogRepository;
import com.codelogium.ticketing.repository.CommentRepository;
import com.codelogium.ticketing.repository.TicketRepository;
import com.codelogium.ticketing.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImp implements CommentService {

    private CommentRepository commentRepository;
    private TicketRepository ticketRepository;
    private AuditLogRepository auditLogRepository;
    private UserRepository userRepository; // only for validation

    @Override
    public Comment createComment(Long ticketId, Long userId, Comment newComment) {
        // TODO: Think a way of avoiding the mutliple calls of different repository
        User author = UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        // ensure user->ticket relationship
        Ticket retrieveTicket = TicketServiceImp.unwrapTicket(ticketId,
                ticketRepository.findByIdAndCreatorId(ticketId, userId));

        newComment.setTicket(retrieveTicket);
        newComment.setAuthor(author);
        newComment.setTimestamp(Instant.now());
        Comment createdComment = commentRepository.save(newComment);

        // Log ticket creation
        auditLogRepository.save(new AuditLog(
                null,
                createdComment.getId(),
                userId,
                "COMMENT_ADDED",
                null,
                null,
                Instant.now()));

        auditLogRepository.flush(); // Ensure immediate persistence

        return createdComment;
    }

    @Override
    public Comment updateComment(Long commentId, Long ticketId, Long userId, Comment newComment) {
        // Verify user existance by checking the creator relationship
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        // Get the ticket and verify it belongs to this user
        TicketServiceImp.unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        Comment retrievedComment = unwrapComment(commentId,
                commentRepository.findByIdAndTicketIdAndAuthorId(commentId, ticketId, userId));

        // TODO: timestamp should be automatically changed when the user has made some
        // changes
        updateIfNotNull(retrievedComment::setAuthor, newComment.getAuthor());
        updateIfNotNull(retrievedComment::setContent, newComment.getContent());
        updateIfNotNull(retrievedComment::setTicket, newComment.getTicket());
        updateIfNotNull(retrievedComment::setTimestamp, newComment.getTimestamp());

        return commentRepository.save(retrievedComment);
    }

    @Override
    public Comment retrieveComment(Long userId, Long ticketId, Long commentId) {
        // Verify user existance by checking the creator relationship
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        // Get the ticket and verify it belongs to this user
        TicketServiceImp.unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        // Attempts to retrieve the corresponding comment
        Comment retrievedComment = unwrapComment(commentId,
                commentRepository.findByIdAndTicketIdAndAuthorId(commentId, ticketId, userId));

        return retrievedComment;
    }

    @Override
    public void removeComment(Long commentId, Long ticketId, Long userId) {

        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        Ticket retrievedTicket = TicketServiceImp.unwrapTicket(ticketId, ticketRepository.findByIdAndCreatorId(ticketId, userId));

        Comment retrievedComment = unwrapComment(commentId,
                commentRepository.findByIdAndTicketIdAndAuthorId(commentId, ticketId, userId));

        retrievedTicket.getComments().remove(retrievedComment);
        ticketRepository.save(retrievedTicket); // triggers orphanRemoval on ticket level

    }

    public static Comment unwrapComment(Long commentId, Optional<Comment> optionalComment) {
        return optionalComment.orElseThrow(() -> new ResourceNotFoundException(commentId, Comment.class));
    }
}
