package com.desafio.evento.controller;

import com.desafio.evento.model.request.EventRequest;
import com.desafio.evento.model.response.EventResponse;
import com.desafio.evento.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateEvent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        EventRequest eventRequest = EventRequest.builder()
                .name("Conference")
                .date(LocalDate.now())
                .location("Conference Hall")
                .maxParticipants(100)
                .build();
        EventResponse eventResponse = EventResponse.builder()
                .id("1")
                .name("Conference")
                .date(LocalDate.now())
                .location("Conference Hall")
                .maxParticipants(100)
                .build();

        when(eventService.saveEvent(any(EventRequest.class))).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = eventController.createEvent(eventRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse.getId(), response.getBody().getId());

        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertTrue(location.toString().contains(eventResponse.getId()));

        verify(eventService, times(1)).saveEvent(any(EventRequest.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetAllEvents() {
        EventResponse event1 = EventResponse.builder().id("1").name("Conference").build();
        EventResponse event2 = EventResponse.builder().id("2").name("Workshop").build();
        List<EventResponse> eventList = Arrays.asList(event1, event2);

        when(eventService.findAll()).thenReturn(eventList);

        ResponseEntity<List<EventResponse>> response = eventController.getAllEvents();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(eventService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetEventById() {
        EventResponse eventResponse = EventResponse.builder().id("1").name("Conference").build();

        when(eventService.findById("1")).thenReturn(Optional.of(eventResponse));

        ResponseEntity<EventResponse> response = eventController.getEventById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse.getId(), response.getBody().getId());

        verify(eventService, times(1)).findById("1");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetEventByIdNotFound() {
        when(eventService.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<EventResponse> response = eventController.getEventById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(eventService, times(1)).findById("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateEvent() {
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Conference")
                .date(LocalDate.now())
                .location("Main Hall")
                .maxParticipants(150)
                .build();
        EventResponse eventResponse = EventResponse.builder()
                .id("1")
                .name("Updated Conference")
                .date(LocalDate.now())
                .location("Main Hall")
                .maxParticipants(150)
                .build();

        when(eventService.updateEvent(eq("1"), any(EventRequest.class))).thenReturn(Optional.of(eventResponse));

        ResponseEntity<EventResponse> response = eventController.updateEvent("1", eventRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse.getId(), response.getBody().getId());

        verify(eventService, times(1)).updateEvent(eq("1"), any(EventRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateEventNotFound() {
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Conference")
                .date(LocalDate.now())
                .location("Main Hall")
                .maxParticipants(150)
                .build();

        when(eventService.updateEvent(eq("1"), any(EventRequest.class))).thenReturn(Optional.empty());

        ResponseEntity<EventResponse> response = eventController.updateEvent("1", eventRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(eventService, times(1)).updateEvent(eq("1"), any(EventRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteEvent() {
        doNothing().when(eventService).deleteById("1");

        ResponseEntity<Void> response = eventController.deleteEvent("1");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(eventService, times(1)).deleteById("1");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testRegisterForEvent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String eventId = "1";
        String userId = "1";

        doNothing().when(eventService).registerForEvent(eventId, userId);

        ResponseEntity<Void> response = eventController.registerForEvent(eventId, userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(eventService, times(1)).registerForEvent(eventId, userId);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUnregisterFromEvent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String eventId = "1";
        String userId = "1";

        doNothing().when(eventService).unregisterFromEvent(eventId, userId);

        ResponseEntity<Void> response = eventController.unregisterFromEvent(eventId, userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(eventService, times(1)).unregisterFromEvent(eventId, userId);
    }
}