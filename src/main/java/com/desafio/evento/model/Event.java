package com.desafio.evento.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private LocalDate date;
    private String location;

    @ManyToMany
    private List<User> participants;

    private int maxParticipants;
    public boolean isFull() {
        return participants.size() >= maxParticipants;
    }
}