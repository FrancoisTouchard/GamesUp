package com.gamesUP.gamesUP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "avis")
@Entity
public class Avis {

	@Id
	@GeneratedValue
	private UUID id;

	String commentaire;
	
	int note;

}
