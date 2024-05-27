package com.desafio.evento.repository;

import com.desafio.evento.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {
}
