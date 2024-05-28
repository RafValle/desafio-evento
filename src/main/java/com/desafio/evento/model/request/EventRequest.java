package com.desafio.evento.model.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EventRequest {
    private String name;
    private LocalDate date;
    private String location;
    private int maxParticipants;
}
