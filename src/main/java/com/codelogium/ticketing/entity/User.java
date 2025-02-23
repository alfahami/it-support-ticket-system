package com.codelogium.ticketing.entity;

import com.codelogium.ticketing.entity.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be null or blank")
    private String username;

    @NotBlank(message = "Password cannot be null or blank")
    private String password;

    @NotBlank(message = "Email cannot be null or blank")
    private String email;

    private UserRole role;

}
