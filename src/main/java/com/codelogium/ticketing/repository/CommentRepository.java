package com.codelogium.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
