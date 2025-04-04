package com.codelogium.ticketing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.codelogium.ticketing.entity.Ticket;
import com.codelogium.ticketing.entity.User;
import com.codelogium.ticketing.entity.enums.Category;
import com.codelogium.ticketing.entity.enums.Priority;
import com.codelogium.ticketing.entity.enums.Status;
import com.codelogium.ticketing.entity.enums.UserRole;
import com.codelogium.ticketing.repository.AuditLogRepository;
import com.codelogium.ticketing.repository.TicketRepository;
import com.codelogium.ticketing.repository.UserRepository;
import com.codelogium.ticketing.service.TicketService;
import com.codelogium.ticketing.service.TicketServiceImp;

@ExtendWith(SpringExtension.class)
public class TicketServiceTest {
    //TODO: implements Ticket Test with roles chekings

    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        ticketService = new TicketServiceImp(ticketRepository, userRepository,auditLogRepository);     
    }

    @Test
    void shouldAddTicketSuccessfully() {

        //Mock
        User testUser = new User(1L, "tupac", "tupac123", "tupac@gmail.com", UserRole.EMPLOYEE, null, null);

        // Status is null in order to test if it being set while ticket saving
        Ticket ticket = new Ticket(1L, "Discrepancy while login", "Error 500 keeps pop up while password is correct", Instant.now(), null, Category.NETWORK, Priority.HIGH, testUser, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // Act
        Ticket createdTicket = ticketService.createTicket(1L, ticket);

        // Assert
        assertNotNull(createdTicket);
        assertEquals("Discrepancy while login", createdTicket.getTitle());
        assertEquals(Status.NEW, createdTicket.getStatus()); // Assure that status was set during ticket creation
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void shouldRetrieveTicketSuccessfully() {
        //Mock
        User testUser = new User(1L, "tupac", "tupac123", "tupac@gmail.com", UserRole.EMPLOYEE, null, null);

        // Status is null in order to test if it being set while ticket saving
        Ticket ticket = new Ticket(1L, "Discrepancy while login", "Error 500 keeps pop up while password is correct", Instant.now(), null, Category.NETWORK, Priority.HIGH, testUser, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.findByIdAndCreatorId(1L, 1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.retrieveTicket(1L, 1L);

        // Assert
        assertEquals(ticket.getId(), result.getId());
        assertEquals(ticket.getTitle(), result.getTitle());

    }
}
