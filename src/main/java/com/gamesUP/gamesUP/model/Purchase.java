package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Table(name = "purchase")
@Entity
public class Purchase {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser user;

	@OneToMany(mappedBy = "purchase")
	private List<PurchaseLine> purchaseLines;

	private LocalDate date;
	private boolean paid;
	private boolean delivered;
	private boolean archived;
	private double totalPrice;
}
