package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

    @ManyToMany
    @JoinTable(
        name = "author_game",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    private String genre;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Publisher publisher;
    private int numEdition;

}
