package com.codelogium.ticketing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndCreatorId(Long ticketId, Long userId);

    List<Ticket> findByCreatorId(Long userId);

    @Query("SELECT t.creator FROM Ticket t WHERE t.id = :ticketId")
    Optional<User> findCreatorByTicket(@Param("ticketId") Long ticketId);
}