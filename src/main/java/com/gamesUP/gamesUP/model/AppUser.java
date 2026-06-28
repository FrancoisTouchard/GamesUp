package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Table(name = "app_user")
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Un utilisateur doit avoir un nom.")
    private String name;

    @NotNull(message = "Un utilisateur doit avoir un mot de passe.")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Wishlist wishlist;

    @OneToMany(mappedBy = "user")
    private List<Avis> avis;
}
