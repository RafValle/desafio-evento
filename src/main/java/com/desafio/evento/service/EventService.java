package com.desafio.evento.service;

import com.desafio.evento.config.exceptions.EventFullException;
import com.desafio.evento.config.exceptions.EventNotFoundException;
import com.desafio.evento.model.Event;
import com.desafio.evento.model.request.EventRequest;
import com.desafio.evento.model.response.EventResponse;
import com.desafio.evento.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        return Optional.ofNullable(eventRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id)));
    }

    public Optional<EventResponse> updateEvent(String id, EventRequest eventUpdateDTO) {
        return Optional.ofNullable(eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setName(eventUpdateDTO.getName());
                    existingEvent.setDate(eventUpdateDTO.getDate());
                    existingEvent.setLocation(eventUpdateDTO.getLocation());
                    existingEvent.setMaxParticipants(eventUpdateDTO.getMaxParticipants());
                    Event updatedEvent = eventRepository.save(existingEvent);
                    return convertToDTO(updatedEvent);
                }).orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id)));
    }

    public void deleteById(String id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new EventNotFoundException("Event not found with id: " + id);
        }
    }

    public void registerForEvent(String eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        if (event.isFull()) {
            throw new EventFullException("Event has reached maximum capacity of participants.");
        }
        event.getParticipants().add(username);
        eventRepository.save(event);
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
