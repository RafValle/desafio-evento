package com.desafio.evento.service;

import com.desafio.evento.config.exceptions.EventFullException;
import com.desafio.evento.config.exceptions.EventNotFoundException;
import com.desafio.evento.model.Event;
import com.desafio.evento.model.request.EventRequest;
import com.desafio.evento.model.response.EventResponse;
import com.desafio.evento.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvent() {
        EventRequest eventRequest = EventRequest.builder()
                .name("Conference")
                .date(LocalDate.now())
                .location("Conference Hall")
                .maxParticipants(100)
                .build();

        Event event = new Event();
        event.setId("1");
        event.setName(eventRequest.getName());
        event.setDate(eventRequest.getDate());
        event.setLocation(eventRequest.getLocation());
        event.setMaxParticipants(eventRequest.getMaxParticipants());

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponse eventResponse = eventService.saveEvent(eventRequest);

        assertNotNull(eventResponse);
        assertEquals(event.getId(), eventResponse.getId());
        assertEquals(event.getName(), eventResponse.getName());
    }

    @Test
    void testFindAll() {
        Event event = new Event();
        event.setId("1");
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));

        var events = eventService.findAll();

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    void testFindById() {
        String eventId = "1";
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Optional<EventResponse> eventResponse = eventService.findById(eventId);

        assertTrue(eventResponse.isPresent());
        assertEquals(eventId, eventResponse.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        String eventId = "1";
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.findById(eventId));
    }

    @Test
    void testUpdateEvent() {
        String eventId = "1";
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Conference")
                .date(LocalDate.now())
                .location("Main Hall")
                .maxParticipants(150)
                .build();

        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Optional<EventResponse> eventResponse = eventService.updateEvent(eventId, eventRequest);

        assertTrue(eventResponse.isPresent());
        assertEquals(eventRequest.getName(), eventResponse.get().getName());
    }

    @Test
    void testUpdateEventNotFound() {
        String eventId = "1";
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Conference")
                .date(LocalDate.now())
                .location("Main Hall")
                .maxParticipants(150)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(eventId, eventRequest));
    }

    @Test
    void testDeleteById() {
        String eventId = "1";
        when(eventRepository.existsById(eventId)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(eventId);

        eventService.deleteById(eventId);

        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void testDeleteByIdNotFound() {
        String eventId = "1";
        when(eventRepository.existsById(eventId)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> eventService.deleteById(eventId));
    }

    @Test
    void testRegisterForEvent() {
        String eventId = "1";
        String username = "user";
        Event event = new Event();
        event.setName("Conference");
        event.setId(eventId);
        event.setMaxParticipants(10);
        event.setParticipants(new ArrayList<>(Collections.singletonList(username)));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.registerForEvent(eventId, username);

        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testRegisterForEventFull() {
        String eventId = "1";
        String username = "user";
        Event event = new Event();
        event.setId(eventId);
        event.setMaxParticipants(1);
        event.setParticipants(Collections.singletonList(username));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(EventFullException.class, () -> eventService.registerForEvent(eventId, username));
    }

    @Test
    void testRegisterForEventNotFound() {
        String eventId = "1";
        String username = "user";

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.registerForEvent(eventId, username));
    }
}