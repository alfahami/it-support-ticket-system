package com.codelogium.ticketing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.entity.enums.Status;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByIdAndCreatorId(Long ticketId, Long userId);

    List<Ticket> findByCreatorId(Long userId);
    // Find creator by ticket id to validates user in this and sub-level
    @Query("SELECT t.creator FROM Ticket t WHERE t.id = :ticketId")
    Optional<User> findCreatorByTicket(@Param("ticketId") Long ticketId);

    // Search by Ticket ID and Status
    @Query("SELECT t FROM Ticket t WHERE (t.id = :ticketId) AND (t.status = :status)")
    Optional<Ticket> findByTicketIdAndStatus(@Param("ticketId") Long ticketId, @Param("status") Status status);
}