package com.codelogium.ticketing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndCreatorId(Long ticketId, Long userId);
    List<Ticket> findByCreatorId(Long userId);
} 