package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

	@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PurchaseLine> purchaseLines;

	@Column(precision = 10, scale = 2)
	private BigDecimal totalPrice;

	private LocalDateTime date;

	@Enumerated(EnumType.STRING)
	private PurchaseStatus status;

}
