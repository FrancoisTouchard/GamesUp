package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "appUser")
@Entity
public class AppUser {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Un utilisateur doit avoir un nom.")
    private String name;

    @NotNull(message = "Un utilisateur doit avoir un mot de passe.")
    private String password;

    @OneToOne(mappedBy = "user")
    private Wishlist wishlist;
}
