package com.codelogium.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.ticketing.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
    List<AuditLog> findByTicketId(Long ticketId);
    List<AuditLog> findByCommentId(Long commentId);
}
