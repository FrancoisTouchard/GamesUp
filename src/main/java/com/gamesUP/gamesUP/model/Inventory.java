package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "inventory")
@Entity
public class Inventory {

	@Id
	@GeneratedValue
	private UUID id;

	@OneToOne
	@JoinColumn(name = "game_id")
	private Game game;

	private int stock;

}
