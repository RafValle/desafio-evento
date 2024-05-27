package com.desafio.evento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "event")
@Entity(name = "event")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String name;
    private Date date;
    private String location;
    private int capacity;
}
