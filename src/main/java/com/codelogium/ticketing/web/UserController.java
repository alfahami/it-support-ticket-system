package com.codelogium.ticketing.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.exception.ErrorResponse;
import com.codelogium.ticketing.service.UserService;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Tag(name = "User Controller", description = "Manages user operations")
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Unsuccessful submission")
    })
    @Operation(summary = "Create User", description = "Registers a new user")
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    // TODO: don't send the whole user entity to the client since there's password
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(ref = "#/components/responses/401") // Gloabl unauthorized response
    })
    @Operation(summary = "Get User", description = "Retrieves a user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<User> retrieveUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.retrieveUser(userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/401") // Gloabl 401 response
    })
    @Operation(summary = "Delete User", description = "Deletes a user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }
}
