package com.desafio.evento.service;

import com.desafio.evento.model.Event;
import com.desafio.evento.model.User;
import com.desafio.evento.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event updateEvent(String id, Event eventDetails) {
        Event event = getEventById(id);
        if (event != null) {
            event.setName(eventDetails.getName());
            event.setDate(eventDetails.getDate());
            event.setLocation(eventDetails.getLocation());
            event.setCapacity(eventDetails.getCapacity());
            return eventRepository.save(event);
        }
        return null;
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public boolean registerUserForEvent(String eventId, User user) {
        Event event = getEventById(eventId);
        if (event != null && event.getCapacity() > 0) {
            event.setCapacity(event.getCapacity() - 1);
            eventRepository.save(event);
            return true;
        }
        return false;
    }
}
