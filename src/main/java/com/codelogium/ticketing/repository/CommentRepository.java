package com.codelogium.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndTicketIdAndAuthorId(Long commentId, Long ticketId, Long userId);
}
