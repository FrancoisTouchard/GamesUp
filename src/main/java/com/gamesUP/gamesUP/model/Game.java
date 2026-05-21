package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "game")
@Entity
public class Game {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Un jeu doit avoir un nom.")
    private String name;

    @NotNull(message = "Un jeu doit avoir un auteur.")
    private String author;

    private String genre;

    @ManyToOne
    private Category category;

    @NotNull(message = "Un jeu doit avoir un éditeur.")
    @ManyToOne
    private Publisher publisher;
    private int numEdition;

}
