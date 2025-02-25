package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.ticketing.entity.Comment;
import com.codelogium.ticketing.service.CommentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users/{userId}/tickets/{ticketId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long ticketId, @PathVariable Long userId, @RequestBody @Valid Comment newComment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(ticketId, userId, newComment));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @PathVariable Long ticketId, @PathVariable Long userId, @RequestBody @Valid Comment newComment) {
        return ResponseEntity.ok(commentService.updateComment(commentId, ticketId, userId, newComment));
    }
}
