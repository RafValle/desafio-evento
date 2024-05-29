package com.desafio.evento.model.response;

import com.desafio.evento.model.UserDTO;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class EventResponse {
    private String id;
    private String name;
    private LocalDate date;
    private String location;
    private int maxParticipants;
    @ElementCollection
    private List<UserDTO> participants;

}
