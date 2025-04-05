package com.codelogium.ticketing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.codelogium.ticketing.dto.TicketInfoUpdateDTO;
import com.codelogium.ticketing.dto.TicketStatusUpdateDTO;
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
    //TODO: implements Ticket Test with roles checking

    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private Ticket testTicket;

    @BeforeEach
    void setUp() throws Exception {
        ticketService = new TicketServiceImp(ticketRepository, userRepository,auditLogRepository);    
        
        testUser = new User(1L, "tupac", "tupac123", "tupac@gmail.com", UserRole.EMPLOYEE, null, null);

        testTicket = new Ticket(1L, "Discrepancy while login", "Error 500 keeps pop up while password is correct", Instant.now(), Status.NEW, Category.NETWORK, Priority.HIGH, testUser, null);
    }

    private void mockBasicUserAndTicketRepo() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.findByIdAndCreatorId(1L, 1L)).thenReturn(Optional.of(testTicket));
    }

    @Test
    void shouldAddTicketSuccessfully() {
        //Mock
        // Status is null in order to test if it being set while ticket saving
        Ticket ticketToCreate = new Ticket(1L, "Discrepancy while login", "Error 500 keeps pop up while password is correct", Instant.now(), null, Category.NETWORK, Priority.HIGH, testUser, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(ticketRepository.save(ticketToCreate)).thenReturn(ticketToCreate);

        // Act
        Ticket result = ticketService.createTicket(1L, ticketToCreate);

        // Assert
        assertNotNull(result);
        assertEquals("Discrepancy while login", result.getTitle());
        assertEquals(Status.NEW, result.getStatus()); // Assure that status was set during ticket creation
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void shouldRetrieveTicketSuccessfully() {
        //Mock
        mockBasicUserAndTicketRepo();

        // Act
        Ticket result = ticketService.retrieveTicket(1L, 1L);

        // Assert
        assertEquals(testTicket.getId(), result.getId());
        assertEquals(testTicket.getTitle(), result.getTitle());
    }

    @Test
    void shouldUpdateTicketInfoSuccessfully() {
        //Mock
        mockBasicUserAndTicketRepo();

        TicketInfoUpdateDTO dto = new TicketInfoUpdateDTO("Can't Login even if password is correct", null, null, Category.OTHER, Priority.MEDIUM);

        Ticket retrievedTicket = ticketRepository.findByIdAndCreatorId(1L, 1L).get();
        when(ticketRepository.save(retrievedTicket)).thenReturn(retrievedTicket);
        
        // Act
        Ticket result = ticketService.updateTicketInfo(1L, 1L, dto);

        // assert
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(testTicket.getDescription(), result.getDescription()); // assert with ticket because description was not changed
        assertEquals(dto.getCategory(), result.getCategory());
        assertEquals(dto.getPriority(), result.getPriority());
    }

    @Test
    void shouldUpdateTicketStatusSuccessfully() {
        //Mock
        mockBasicUserAndTicketRepo();

        TicketStatusUpdateDTO dto = new TicketStatusUpdateDTO(Status.IN_PROGRESS);

        Ticket retrievedTicket = ticketRepository.findByIdAndCreatorId(1L, 1L).get();

        when(ticketRepository.save(retrievedTicket)).thenReturn(retrievedTicket);

        // Act
        Ticket result = ticketService.updateTicketStatus(1L, 1L, dto);

        // assert
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(retrievedTicket.getTitle(), result.getTitle());  
    }

    @Test
    void shouldRemoveTicketSuccessfully() {
        // Mock
        mockBasicUserAndTicketRepo();
        // assure that user have its ticket list, as orphan removal will delete the tciket once it removed from user ticket list
        testUser.setTickets(new ArrayList<>(List.of(testTicket)));
        when(ticketRepository.save(testTicket)).thenReturn(testTicket);

        // Act
        ticketService.removeTicket(1L, 1L);

        // Assert
        assertFalse(testUser.getTickets().contains(testTicket));
        verify(userRepository, times(1)).save(testUser);
        verify(ticketRepository, never()).delete(any());
    }

}
