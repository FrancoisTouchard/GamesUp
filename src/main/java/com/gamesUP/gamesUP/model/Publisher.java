package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

}
