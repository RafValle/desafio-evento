package com.desafio.evento.service;

import com.desafio.evento.model.Event;
import com.desafio.evento.model.request.EventRequest;
import com.desafio.evento.model.response.EventResponse;
import com.desafio.evento.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private EventRepository eventRepository;


    public EventResponse saveEvent(EventRequest eventCreateDTO) {
        Event event = new Event();
        event.setName(eventCreateDTO.getName());
        event.setDate(eventCreateDTO.getDate());
        event.setLocation(eventCreateDTO.getLocation());
        event.setMaxParticipants(eventCreateDTO.getMaxParticipants());
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventResponse> findById(String id) {
        return eventRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<EventResponse> updateEvent(String id, EventRequest eventUpdateDTO) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setName(eventUpdateDTO.getName());
                    existingEvent.setDate(eventUpdateDTO.getDate());
                    existingEvent.setLocation(eventUpdateDTO.getLocation());
                    existingEvent.setMaxParticipants(eventUpdateDTO.getMaxParticipants());
                    Event updatedEvent = eventRepository.save(existingEvent);
                    return convertToDTO(updatedEvent);
                });
    }

    public ResponseEntity<Void> deleteById(String id) {
        eventRepository.findById(id)
                .map(existingEvent -> {
                    eventRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());

        return null;
    }

    public ResponseEntity<Void> registerForEvent(String eventId, String username) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.isFull()) {
                return ResponseEntity.badRequest().build();
            }
            event.getParticipants().add(username);
            eventRepository.save(event);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EventResponse convertToDTO(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .date(event.getDate())
                .location(event.getLocation())
                .participants(event.getParticipants())
                .maxParticipants(event.getMaxParticipants())
                .build();
    }
}
