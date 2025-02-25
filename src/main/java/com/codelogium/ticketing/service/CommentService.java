package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.Comment;

public interface CommentService {
    Comment createComment(Long ticketId, Long userId, Comment comment);
    Comment updateComment(Long commentId, Long ticketId, Long userId, Comment newComment);
    Comment retrieveComment(Long userId, Long ticketId, Long commentId);
} 
