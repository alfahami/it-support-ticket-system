package com.codelogium.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    
} 