package com.codelogium.ticketing.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codelogium.ticketing.entity.AuditLog;
import com.codelogium.ticketing.entity.Comment;
import com.codelogium.ticketing.service.CommentService;
import com.codelogium.ticketing.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Tag(name = "Comment Controller", description = "Manages comments on tickets")
@RequestMapping(value = "/users/{userId}/tickets/{ticketId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

        private final CommentService commentService;

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Comment successfully created"),
                        @ApiResponse(responseCode = "400", description = "Bad Request: Unsuccessful submission"),
                        @ApiResponse(ref = "#/components/responses/401")
        })
        @Operation(summary = "Create Comment", description = "Adds a new comment to a ticket")
        @PostMapping
        public ResponseEntity<Comment> createComment(
                        @PathVariable Long ticketId,
                        @PathVariable Long userId,
                        @RequestBody @Valid Comment newComment) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(commentService.createComment(ticketId, userId, newComment));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comment successfully updated"),
                        @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(ref = "#/components/responses/401")
        })
        @Operation(summary = "Update Comment", description = "Updates an existing comment")
        @PatchMapping("/{commentId}")
        public ResponseEntity<Comment> updateComment(
                        @PathVariable Long commentId,
                        @PathVariable Long ticketId,
                        @PathVariable Long userId,
                        @RequestBody @Valid Comment newComment) {
                return ResponseEntity.ok(commentService.updateComment(commentId, ticketId, userId, newComment));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comment successfully retrieved", content = @Content(schema = @Schema(implementation = Comment.class))),
                        @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(ref = "#/components/responses/401")
        })
        @Operation(summary = "Get Comment", description = "Retrieves a comment by ID")
        @GetMapping("/{commentId}")
        public ResponseEntity<Comment> retrieveComment(
                        @PathVariable Long commentId,
                        @PathVariable Long ticketId,
                        @PathVariable Long userId) {
                return ResponseEntity.ok(commentService.retrieveComment(userId, ticketId, commentId));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comment Logs successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuditLog.class)))),
                        @ApiResponse(responseCode = "404", description = "User, Ticket or Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(ref = "#/components/responses/401")
        })
        @Operation(summary = "Audit Comment Log", description = "Retrieves Comments' Audit Log")
        @GetMapping("/{commentId}/audit-logs")
        public ResponseEntity<List<AuditLog>> retrieveAuditLogs(@PathVariable Long commentId,
                        @PathVariable Long ticketId, @PathVariable Long userId) {
                return ResponseEntity.ok(commentService.retrieveAuditLogs(commentId, ticketId, userId));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Comment successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(ref = "#/components/responses/401")
        })
        @Operation(summary = "Delete Comment", description = "Deletes a comment by ID")
        @DeleteMapping("/{commentId}")
        public ResponseEntity<Void> removeComment(
                        @PathVariable Long commentId,
                        @PathVariable Long ticketId,
                        @PathVariable Long userId) {
                commentService.removeComment(commentId, ticketId, userId);
                return ResponseEntity.noContent().build();
        }
}
