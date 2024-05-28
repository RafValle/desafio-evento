package com.desafio.evento.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date date;
    private String location;
    private int capacity;

}
