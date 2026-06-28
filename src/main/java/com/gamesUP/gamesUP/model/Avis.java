package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "avis")
@Entity
public class Avis {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String comment;

	@NotNull(message = "La note est obligatoire.")
	@Min(value = 1, message = "La note ne peut pas être inférieure à 1.")
	@Max(value = 10, message = "La note ne peut pas être supérieure à 10.")
	private Integer rating;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser user;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;
}
