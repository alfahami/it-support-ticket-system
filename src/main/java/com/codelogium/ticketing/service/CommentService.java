package com.codelogium.ticketing.service;

import com.codelogium.ticketing.entity.Comment;

public interface CommentService {
    Comment createComment(Long ticketId, Long userId, Comment comment);
} 
